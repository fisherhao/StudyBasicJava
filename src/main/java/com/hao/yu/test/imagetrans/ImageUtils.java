import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * 图片压缩工具（固定输出JPG，仅调整质量，不改变尺寸）
 * 使用流式输入输出，自动识别源格式，无视错误后缀。
 */
public class ImageUtils {

    /**
     * 压缩图片为 JPG 格式，仅调整压缩质量（0~100%），保持原始尺寸。
     *
     * @param sourceFile
     *     源图片文件（支持 JPEG、PNG、WebP、BMP、GIF 等）
     * @param qualityPercent
     *     压缩质量百分比，例如 50 表示 50%（0~100）
     * @param targetFile
     *     目标 JPG 文件（必须为 .jpg 或 .jpeg 后缀）
     *
     * @throws IOException
     *     读写或处理出错时抛出
     */
    public static void compressToJpg(File sourceFile, int qualityPercent, File targetFile) throws IOException {
        // 参数校验
        if (qualityPercent < 0 || qualityPercent > 100) {
            throw new IllegalArgumentException("qualityPercent 必须在 0~100 之间");
        }
        float quality = qualityPercent / 100.0f; // 转为 0.0~1.0

        // 1. 流式读取：自动识别格式
        BufferedImage image = null;
        ImageReader reader = null;
        ImageInputStream inputStream = null;
        try {
            inputStream = ImageIO.createImageInputStream(sourceFile);
            if (inputStream == null) {
                throw new IOException("无法创建输入流，文件可能不存在或格式不支持");
            }
            // 获取所有可用的 ImageReader
            Iterator<ImageReader> readers = ImageIO.getImageReaders(inputStream);
            if (!readers.hasNext()) {
                throw new IOException("未找到可解码该图片的 Reader");
            }
            reader = readers.next();
            reader.setInput(inputStream, true, true); // 流式读取，允许只读元数据

            // 读取图片（全图加载，但这是必要的，因为JPEG编码需要全图像素）
            image = reader.read(0);
            if (image == null) {
                throw new IOException("读取图片失败，可能是空图片");
            }
        } finally {
            // 关闭输入流和释放 reader
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
            if (reader != null) {
                reader.dispose();
            }
        }

        // 2. 流式写入 JPG（带压缩质量）
        ImageWriter writer = null;
        ImageOutputStream outputStream = null;
        try {
            outputStream = ImageIO.createImageOutputStream(targetFile);
            if (outputStream == null) {
                throw new IOException("无法创建输出流，目标路径可能无效");
            }
            writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            writer.setOutput(outputStream);

            ImageWriteParam param = writer.getDefaultWriteParam();
            // JPEG 支持压缩
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality);
            } else {
                // 保险，但通常 JPEG 都支持
            }

            // 写入图片
            writer.write(null, new IIOImage(image, null, null), param);
            // 强制刷新到文件
            outputStream.flush();
        } finally {
            // 释放资源
            if (image != null) {
                image.flush(); // 释放图像数据
            }
            if (writer != null) {
                writer.dispose();
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    /**
     * 简易调用示例（main 测试方法）
     */
    public static void main(String[] args) {
        // 使用示例：压缩本地图片
        File src = new File("D:/test/input.png");      // 源文件（可以是 png/webp 等）
        File dest = new File("D:/test/output.jpg");     // 目标文件，固定 .jpg
        int quality = 30;                               // 压缩质量 30%

        try {
            System.out.println("开始压缩...");
            long start = System.currentTimeMillis();
            compressToJpg(src, quality, dest);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成！耗时 " + (end - start) + " ms");
            System.out.println("输出文件大小：" + dest.length() / 1024 + " KB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}