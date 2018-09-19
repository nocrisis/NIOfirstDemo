package Transport.messageDTO;

import com.alibaba.fastjson.JSON;

public class ResultInfo {

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
