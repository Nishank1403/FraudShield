package com.fraudshield.controller;

import com.fraudshield.store.TransactionResultStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final TransactionResultStore resultStore;

    public DashboardController(TransactionResultStore resultStore) {
        this.resultStore = resultStore;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("results", resultStore.listRecent(50));
        return "dashboard";
    }
}
