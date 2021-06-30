import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;

public class AmazonWebServiceUtil {

    private static final String BUCKET_NAME = "s3-upload-bucket-kim"; // AWS 에 설정된 버킷명
    private static final String ACCESS_KEY = "AKIARK2WHJHXKQLEHAOY"; // AWS에 접근하기 위한 키
    private static final String SECRET_KEY = "DJE3unacJvlpLl88n0II9cEO5NIzB0v2vgPj9+ZX"; // AWS에 접근하기 위한 비밀 키
    private AmazonS3 amazonS3;

    public AmazonWebServiceUtil() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        amazonS3 = new AmazonS3Client(awsCredentials);
        amazonS3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2)); // 추가된 코드
        System.out.println("객체가 이동합니다.");
    }

    public void uploadFile(File file) {
        if (amazonS3 != null) {
            try {
//                하위폴더 없을때 경로
//                PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, file.getName(), file);
//              하위폴더 있을때 경로 ex)S3 버킷 /test 가된다...
                PutObjectRequest putObjectRequest = new PutObjectRequest(
                        BUCKET_NAME + "/test", // 버킷 이하 하위 폴더까지 지정
                        file.getName(), // 무조건 파일 명만!
                        file // 실제 파일
                );

                putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead); // file permission
                amazonS3.putObject(putObjectRequest); // upload file
            } catch (AmazonServiceException ase) {
                ase.printStackTrace();
            } finally {
                amazonS3 = null;
                System.out.println("객체가 이동 완료 되었습니다.");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String fileName = "d:\\20210628_173218.jpg";
        File tempFile = new File(fileName);
        AmazonWebServiceUtil awsService = new AmazonWebServiceUtil();
        awsService.uploadFile(tempFile);
        System.out.println("객체이동 성공");
    }
}