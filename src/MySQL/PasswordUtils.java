package MySQL;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // Mã hóa mật khẩu
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));
    }

    // Kiểm tra mật khẩu
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
/*nghiên cứu về chữ ký số tích hợp với PDF, Word
 */
// 2 chiều 1 key chữ ký số 2 key