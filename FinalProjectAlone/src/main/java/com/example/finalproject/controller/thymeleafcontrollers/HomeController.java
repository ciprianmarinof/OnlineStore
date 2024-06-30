package com.example.finalproject.controller.thymeleafcontrollers;

import com.example.finalproject.entity.Category;
import com.example.finalproject.service.CategoryServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private final CategoryServiceImpl categoryServiceImpl;

    public HomeController(CategoryServiceImpl categoryServiceImpl) {
        this.categoryServiceImpl = categoryServiceImpl;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to the Final Project Application");
        return "index";
    }

    @GetMapping("/products")
    public String products(Model model) {
        // Fetch products from database and add to the model
        return "products";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        // Fetch orders from database and add to the model
        return "orders";
    }

    @GetMapping("/users")
    public String users(Model model) {
        // Fetch users from database and add to the model
        return "users";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register/user")
    public String register(@RequestParam(value = "success", required = false) String success, Model model) {
        if(success != null) {
            model.addAttribute("message", "Registration successful. Please login.");
        }

        return "register";
    }

    @GetMapping("/categories/show")
    public String showCategories(Model model){
        List<Category> categories = categoryServiceImpl.getAllCategoriesWithoutParent();
        model.addAttribute("categories", categories);

        return "categories";
    }
}
