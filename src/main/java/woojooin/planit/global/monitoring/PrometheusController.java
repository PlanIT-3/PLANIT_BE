package woojooin.planit.global.monitoring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PrometheusController {

	private final PrometheusMeterRegistry registry;

	@GetMapping(value = "/prometheus", produces = "text/plain; version=0.0.4; charset=utf-8")
	@ResponseBody
	public String scrape() {
		return registry.scrape();
	}

}
