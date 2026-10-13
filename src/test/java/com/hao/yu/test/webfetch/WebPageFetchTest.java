package com.hao.yu.test.webfetch;

import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * 网页转 Markdown 测试工具（学习用）。
 * <p>
 * 功能：给定一个网页链接，抓取 HTML 并转换为 Markdown 文件，保存在运行目录下。
 * <p>
 * 图片处理策略：转换前直接删除所有 img 元素——不保留任何图片引用，
 * 保证 Markdown 文件复制到哪里内容都完整（引用式图片一旦离开原网页就全部失效）。
 * <p>
 * 使用方式：直接运行 main 方法，把目标网址写进 DEFAULT_URL 常量（或通过 args[0] 传入），
 * 也可以在其他代码里调用 {@link #fetchAsMarkdown(String)} 获取 Markdown 文本，
 * 或调用 {@link #saveAsMarkdownFile(String)} 直接落盘并拿到文件路径。
 *
 * @author Witty·Kid Fisher
 * @version 0.0.3
 */
public class WebPageFetchTest {

    /**
     * 默认抓取目标：直接改这里即可，或运行时通过 args[0] 覆盖
     */
    private static final String DEFAULT_URL = "https://www.example.com";

    /**
     * 连接/读取超时时间（毫秒）
     */
    private static final int TIMEOUT_MS = 8000;

    /**
     * 模拟浏览器 User-Agent，避免被目标站点拦截
     */
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) "
            + "Chrome/126.0.0.0 Safari/537.36";

    /**
     * 生成的 Markdown 文件名最大长度（不含扩展名），超长截断避免文件名过长
     */
    private static final int MAX_FILE_NAME_LENGTH = 60;

    /**
     * Windows/macOS 文件名不允许出现的字符，替换为下划线
     */
    private static final String ILLEGAL_FILE_NAME_CHARS = "[\\\\/:*?\"<>|\\s]+";

    /**
     * 抓取网页并转换为 Markdown 文本：图片元素在转换前删除，不产生任何图片引用
     *
     * @param url 网页链接
     * @return Markdown 文本（含标题、描述、正文）
     * @throws IOException 网络请求失败或 HTTP 状态异常时抛出
     */
    public static String fetchAsMarkdown(String url) throws IOException {
        Document document = fetchDocument(url);
        String title = document.title().trim();
        String description = metaContent(document, "meta[name=description]");
        if (description.isEmpty()) {
            description = metaContent(document, "meta[property=og:description]");
        }

        // 删除所有图片及其外层容器：转换后的 Markdown 不保留任何图片引用
        document.select("img, picture, source, figure").remove();

        // flexmark 负责剩余 HTML 结构到 Markdown 的转换（标题、列表、表格、代码块、链接等）
        String markdown = FlexmarkHtmlConverter.builder().build().convert(document.body()).trim();

        // 头部拼上元信息块：标题做一级标题，描述与来源做引用块
        StringBuilder result = new StringBuilder();
        result.append("# ").append(title.isEmpty() ? url : title).append("\n\n");
        if (!description.isEmpty()) {
            result.append("> ").append(description).append("\n\n");
        }
        result.append("> 来源：").append(url).append("\n\n");
        result.append("---\n\n").append(markdown).append("\n");
        return result.toString();
    }

    /**
     * 抓取网页转换为 Markdown 并保存为文件，文件名取自页面标题
     *
     * @param url 网页链接
     * @return 生成的文件绝对路径
     * @throws IOException 网络请求或文件写入失败时抛出
     */
    public static String saveAsMarkdownFile(String url) throws IOException {
        String markdown = fetchAsMarkdown(url);

        // 文件名：优先用页面标题，清洗非法字符并截断；标题为空时回退域名
        String fileName = cleanFileName(extractTitle(markdown));
        fileName = fileName + System.currentTimeMillis();
        Path outputFile = Paths.get(System.getProperty("user.dir"), fileName + ".md");
        Files.write(outputFile, markdown.getBytes(StandardCharsets.UTF_8));
        return outputFile.toAbsolutePath().toString();
    }

    /**
     * 抓取网页并解析为 jsoup 文档对象：字符集嗅探与容错解析由 jsoup 内部完成
     *
     * @param url 网页链接
     * @return 解析后的 Document
     * @throws IOException 网络请求失败或 HTTP 状态异常时抛出
     */
    private static Document fetchDocument(String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        connection.timeout(TIMEOUT_MS);
        connection.userAgent(USER_AGENT);
        // 遇到非 2xx 状态码直接抛异常，避免拿到错误页还继续解析
        connection.ignoreHttpErrors(false);
        return connection.get();
    }

    /**
     * 按 CSS 选择器读取 meta 标签的 content 值
     *
     * @param document jsoup 文档
     * @param cssQuery CSS 选择器表达式
     * @return content 值；元素不存在时返回空字符串
     */
    private static String metaContent(Document document, String cssQuery) {
        Element meta = document.selectFirst(cssQuery);
        return meta == null ? "" : meta.attr("content").trim();
    }

    /**
     * 从生成的 Markdown 文本中取出一级标题
     *
     * @param markdown Markdown 文本
     * @return 一级标题内容；不存在时返回空字符串
     */
    private static String extractTitle(String markdown) {
        for (String line : markdown.split("\n")) {
            if (line.startsWith("# ")) {
                return line.substring(2).trim();
            }
        }
        return "";
    }

    /**
     * 清洗文件名：替换非法字符、截断超长标题；清洗后为空时回退时间戳
     *
     * @param title 页面标题
     * @return 安全可用的文件名（不含扩展名）
     */
    private static String cleanFileName(String title) {
        String cleaned = title.replaceAll(ILLEGAL_FILE_NAME_CHARS, "_").trim();
        if (cleaned.length() > MAX_FILE_NAME_LENGTH) {
            cleaned = cleaned.substring(0, MAX_FILE_NAME_LENGTH);
        }
        return cleaned.isEmpty() ? "webpage_" + System.currentTimeMillis() : cleaned;
    }

    /**
     * 本地验证入口：运行后从控制台读取要抓取的网页链接，直接回车则使用 DEFAULT_URL；
     * 控制台只输出最终生成的文件路径，Markdown 内容全部落盘
     *
     * @param args 命令行参数（未使用）
     * @throws IOException 抓取或写文件失败时抛出
     */
    public static void main(String[] args) throws IOException {
        System.out.print("请输入网页链接（直接回车默认抓取 " + DEFAULT_URL + "）：");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
        String url = input.isEmpty() ? DEFAULT_URL : input;

        System.out.println("生成文件：" + saveAsMarkdownFile(url));

    }
}
