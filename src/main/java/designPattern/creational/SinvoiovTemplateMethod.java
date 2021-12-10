package designPattern.creational;

import java.util.HashMap;
import java.util.Map;

/**
 * 模板为核心，抽象工厂为调用的一种实现
 */
public abstract class SinvoiovTemplateMethod {
    //模板模式
    public SinvoiovResp execute(Map params) {
        final SinvoiovResp result = call(params);

        if (result.getStatus() == 1016) {
//            final String newToken = LoginState.refreshToken();
            final String newToken = "123";
            if (newToken == null || "".equals(newToken)) {
                return result;
            } else {
                return call(params);
            }
        } else {
            return result;
        }
    }

    protected abstract SinvoiovResp call(Map params);

    public static void main(String[] args) {
        final HashMap<Object, Object> map = new HashMap<>();
//        final SinvoiovResp sinvoiovResp = new SinvoiovGetXXX().execute(map);
//        System.out.println(sinvoiovResp);

        final SinvoiovResp execute = SinvoiovApiFactory.getInstance(SinvoiovApiEnum.XXX).execute(map);
        System.out.println(execute);
    }
}

class SinvoiovGetXXX extends SinvoiovTemplateMethod {

    int i = 0;

    @Override
    protected SinvoiovResp call(Map params) {
        final SinvoiovResp sinvoiovResp = new SinvoiovResp();

        if (i++ == 1) {
            sinvoiovResp.setResult("OK");
            sinvoiovResp.setStatus(1001);
        } else {
            sinvoiovResp.setResult("ERROR");
            sinvoiovResp.setStatus(1016);
        }
        return sinvoiovResp;
    }
}

class SinvoiovGetYYY extends SinvoiovTemplateMethod {

    int i = 0;

    @Override
    protected SinvoiovResp call(Map params) {
        final SinvoiovResp sinvoiovResp = new SinvoiovResp();

        if (i++ == 1) {
            sinvoiovResp.setResult("this is 'yyy' return");
            sinvoiovResp.setStatus(1001);
        } else {
            sinvoiovResp.setResult("ERROR");
            sinvoiovResp.setStatus(1016);
        }
        return sinvoiovResp;
    }
}

//抽象工厂
abstract class SinvoiovApiFactory {
    private static final SinvoiovApiFactory_XXX xxx = new SinvoiovApiFactory_XXX();
    private static final SinvoiovApiFactory_YYY yyy = new SinvoiovApiFactory_YYY();

    static SinvoiovApiFactory getInstance(SinvoiovApiEnum sinvoiovApiEnum) {
        SinvoiovApiFactory factory = null;
        switch (sinvoiovApiEnum) {
            case XXX:
                factory = xxx;
                break;
            case YYY:
                factory = yyy;
                break;
        }
        return factory;
    }

    protected abstract SinvoiovResp execute(Map params);
}

class SinvoiovApiFactory_XXX extends SinvoiovApiFactory {
    private SinvoiovGetXXX sinvoiovGetXXX = new SinvoiovGetXXX();

    @Override
    protected SinvoiovResp execute(Map params) {
        return sinvoiovGetXXX.execute(params);
    }
}

class SinvoiovApiFactory_YYY extends SinvoiovApiFactory {
    private SinvoiovGetYYY sinvoiovGetYYY = new SinvoiovGetYYY();

    @Override
    protected SinvoiovResp execute(Map params) {
        return sinvoiovGetYYY.execute(params);
    }
}

enum SinvoiovApiEnum {
    XXX, YYY;
}

//POJO
class SinvoiovResp {
    private Integer status;
    private Object result;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "SinvoiovResp{" +
                "status=" + status +
                ", result=" + result +
                '}';
    }
}