import java.io.File;
import java.net.URLEncoder;

public class RequestBaidu {

    public static String ocr(String filePath, String apiList , String accessToken)  {
		if(!new File(filePath).isFile()){
			return "路径错误";
		}
        try {
            byte[] imgData = OcrFileUtil.readFileByBytes(filePath);
            String imgStr = OcrBase64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;
			return OcrHttpUtil.post(apiList, accessToken, param);
        } catch (Exception e) {
            return "链接参数错误";
        }
	}

}
