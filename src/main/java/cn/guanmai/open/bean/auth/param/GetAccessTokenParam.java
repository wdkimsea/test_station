package cn.guanmai.open.bean.auth.param;

public class GetAccessTokenParam {  // 使用v1接口 station用户来获取access_token
    private String appid;
    private String secret;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public GetAccessTokenParam(String appid, String secret){
        super();
        this.appid = appid;
        this.secret = secret;
    }
}

