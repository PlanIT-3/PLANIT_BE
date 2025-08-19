package woojooin.planit.global.monitoring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PrometheusController {

	private final PrometheusMeterRegistry registry;

	@GetMapping(value = "/test/prometheus", produces = "text/plain; version=0.0.4; charset=utf-8")
	@ResponseBody
	public String scrape() {
		return registry.scrape();
	}

}
