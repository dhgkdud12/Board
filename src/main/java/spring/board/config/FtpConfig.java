package spring.board.config;

import lombok.Getter;

@Getter
public class FtpConfig {
    //ftp   ip
    private final String FTP_ADDRESS = "192.168.1.110";
    //
    private final int FTP_PORT = 21;
    //
    private final String FTP_USERNAME = "aaa";
    //
    private final String FTP_PASSWORD = "aaa";
    //
    private final String FTP_BASEPATH = "/images/";
}
