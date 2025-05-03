package storemanagement.example.group_15.infrastructure.helper;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import storemanagement.example.group_15.infrastructure.error.AppException;

@Component
public class OtpEmailHelper {
    @Autowired
    private MailHelper mailHelper;

    public void sendOtpEmail(String recipientEmail, String username, String otpCode)  {
    try{
        String htmlContent = String.format("""
            <html>
                <body style="font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;">
                    <div style="text-align: center;">
                        <h2 style="color: #2e6c80;">Xác nhận mã OTP</h2>
                    </div>
                    <p>Xin chào <strong>%s</strong>,</p>
                    <p>Chúng tôi đã nhận được yêu cầu xác thực tài khoản của bạn. Mã OTP của bạn là:</p>
                    <div style="text-align: center; margin: 20px 0;">
                        <span style="font-size: 24px; font-weight: bold; color: #ffffff; background-color: #4CAF50; padding: 10px 20px; border-radius: 5px; display: inline-block;">
                            %s
                        </span>
                    </div>
                    <p>Vui lòng nhập mã này trong vòng <strong>5 phút</strong> để hoàn tất quá trình xác thực.</p>
                    <p>Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email hoặc liên hệ với chúng tôi qua <a href="mailto:support@example.com">support@example.com</a>.</p>
                    <p>Trân trọng,<br>Đội ngũ hỗ trợ</p>
                    <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
                    <p style="font-size: 12px; color: #777; text-align: center;">
                        Đây là email tự động, vui lòng không trả lời trực tiếp.
                    </p>
                </body>
            </html>
            """, username, otpCode);
        mailHelper.sendMail(
                recipientEmail,
                "Mã OTP xác thực tài khoản",
                htmlContent,
                true
        );
    } catch (MessagingException e) {
        throw new AppException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
    }
    }
}