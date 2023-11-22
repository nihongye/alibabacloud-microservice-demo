package com.alibaba.edas;

import com.taobao.hsf.app.api.util.HSFApiConsumerBean;
import com.taobao.hsf.remoting.service.GenericService;
import com.taobao.hsf.util.PojoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.alibaba.boot.hsf.Constants.DEFAULT_GROUP;

@RestController public class SimpleController {

    @Autowired private HelloService helloService;
    @Autowired private ProductService productService;
    @Autowired
    private HealthService healthService;

    @RequestMapping(value = "/hello/echo/{str}", method = RequestMethod.GET) public String echo(@PathVariable String str)
        throws Exception {
        return helloService.echo(str);
    }

    @RequestMapping(value = "/hello/sleep/{time}", method = RequestMethod.GET) public String sleep(@PathVariable int time)
        throws Exception {
        return helloService.sleep(time) + "";
    }

    @RequestMapping(value = "/hello/exception/{message}", method = RequestMethod.GET) public String exception(@PathVariable String message)
        throws Exception {
        helloService.exception(message);
        return null;
    }

    @RequestMapping(value = "/hello/runtimeexception/{message}", method = RequestMethod.GET) public String runtimeexception(@PathVariable String message)
        throws Exception {
        helloService.runtimeException(message);
        return null;
    }

    @RequestMapping(value = "/generic/hello/echo/{str}", method = RequestMethod.GET)
    public String genericEcho(@PathVariable String str) throws Throwable {
        HSFApiConsumerBean bean = buildConsumerBean();
        GenericService service = (GenericService)bean.getObject();

        Object result = service.$invoke("echo", new String[] {String.class.getName()}, new Object[] {str});
        boolean error = PojoUtils.isGenericBizException(result);
        if (error) {
            Throwable t = PojoUtils.getGenericBizException(result);
            if (t != null) {
                throw t;
            }
        }
        return result.toString();
    }

    @RequestMapping(value = "/generic/hello/invoke/{method}", method = RequestMethod.GET)
    public String genericeInvoke(@PathVariable String method) throws Throwable {
        HSFApiConsumerBean bean = buildConsumerBean();
        GenericService service = (GenericService)bean.getObject();

        Object result = service.$invoke(method, new String[] {String.class.getName()}, new Object[] {method});
        boolean error = PojoUtils.isGenericBizException(result);
        if (error) {
            Throwable t = PojoUtils.getGenericBizException(result);
            throw t;
        }
        return result.toString();
    }

    @RequestMapping(value = "/product/getInfo/{name}", method = RequestMethod.GET)
    public String product(@PathVariable String name) throws Exception {
        return productService.getInfo(name);
    }

    @RequestMapping(value = "/health/update/{state}", method = RequestMethod.GET)
    public String updateHealth(@PathVariable String state) throws Exception{
        return healthService.update(state);
    }

    public HSFApiConsumerBean buildConsumerBean() throws Exception {
        HSFApiConsumerBean bean = new HSFApiConsumerBean();

        bean.setInterfaceName(HelloService.class.getName());
        bean.setVersion("1.0.0");
        bean.setGroup(DEFAULT_GROUP);
        bean.setGeneric("true");

        bean.setClientTimeout(3000);
        bean.init(0);

        return bean;
    }
}
