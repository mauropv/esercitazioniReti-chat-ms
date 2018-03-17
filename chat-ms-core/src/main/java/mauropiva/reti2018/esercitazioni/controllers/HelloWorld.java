package mauropiva.reti2018.esercitazioni.controllers;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import mauropiva.reti2018.esercitazioni.beans.ShallowBean;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path="/chat-ms-core")
public class HelloWorld {

    public static final String BASE_VERSION = "v1";

    private final Timer helloTimer;

    public HelloWorld(MetricRegistry metricRegistry) {
        this.helloTimer = metricRegistry.timer("mauropiva.reti2018.esercitazioni.metrics.hello");
    }

    @RequestMapping(value = "/" + BASE_VERSION + "/hello", method = GET)
    public ShallowBean helloWorld() {
        Timer.Context timer = helloTimer.time();
        ShallowBean bean = new ShallowBean("world!");
        try {
            //do something
        } finally {
            timer.stop();
        }
        return bean;
    }
}