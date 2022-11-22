package spring.board.API.controller;

import org.springframework.web.bind.annotation.*;
import spring.board.API.domain.AiResults;
import spring.board.API.service.ApiService;


@RestController
@RequestMapping("/ssyr/hopper/ai-results")
public class ApiController {

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping(value = "")
    public void resultList() {
        apiService.findResult();
    }

    @PostMapping(value = "")
    public void resultSave(@RequestBody AiResults results) {
        apiService.saveResult(results);
    }
}
