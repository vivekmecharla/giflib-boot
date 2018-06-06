package com.vivek.giflibboot.web.controller;

import com.vivek.giflibboot.data.CategoryRepository;
import com.vivek.giflibboot.data.GifRepository;
import com.vivek.giflibboot.model.Category;
import com.vivek.giflibboot.web.Color;
import com.vivek.giflibboot.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private GifRepository gifRepository;

    // Index of all categories
    @RequestMapping("/categories")
    public String listCategories(Model model) {
        List<Category> categories = (List<Category>) categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "category/index";
    }

    // Single category page
    @RequestMapping("/categories/{categoryId}")
    public String category(@PathVariable Long categoryId, Model model) {
        Category category = categoryRepository.findById(categoryId);
        model.addAttribute("category", category);
        return "category/details";
    }

    // Form for adding a new category
    @RequestMapping("/categories/add")
    public String formNewCategory(Model model) {
        if (!model.containsAttribute("category")) {
            model.addAttribute("category", new Category());
        }
        model.addAttribute("colors", Color.values());
        model.addAttribute("action", "/categories");
        model.addAttribute("heading", "New Category");
        model.addAttribute("submit", "Add");
        return "category/form";
    }

    // Form for editing an existing category
    @RequestMapping("categories/{categoryId}/edit")
    public String formEditCategory(@PathVariable Long categoryId, Model model) {
        if (!model.containsAttribute("category")) {
            model.addAttribute("category", categoryRepository.findById(categoryId));
        }
        model.addAttribute("colors", Color.values());
        model.addAttribute("action", String.format("/categories/%s", categoryId));
        model.addAttribute("heading", "Edit Category");
        model.addAttribute("submit", "Update");
        return "category/form";
    }

    // Update an existing category
    @RequestMapping(value = "/categories/{categoryId}", method = RequestMethod.POST)
    public String updateCategory(@Valid Category category, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category", result);
            redirectAttributes.addFlashAttribute("category", category);
            return String.format("redirect:/categories/%s/edit", category.getId());
        }
        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Category Successfully updated!", FlashMessage.Status.SUCCESS));
        return "redirect:/categories";
    }

    // Add a category
    @RequestMapping(value = "/categories", method = RequestMethod.POST)
    public String addCategory(@Valid Category category, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category", result);
            redirectAttributes.addFlashAttribute("category", category);
            return "redirect:/categories/add";
        }
        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Category Successfully added!", FlashMessage.Status.SUCCESS));
        return "redirect:/categories";
    }

    // Delete an existing category
    @RequestMapping(value = "/categories/{categoryId}/delete", method = RequestMethod.POST)
    public String deleteCategory(@PathVariable Long categoryId, RedirectAttributes redirectAttributes) {
        Category category = new Category();
        category.setId(categoryId);
        if (gifRepository.findByCategory(category).size() > 0) {
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Only empty categories can be deleted!", FlashMessage.Status.FAILURE));
        } else {
            categoryRepository.delete(category);
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Category deleted Successfully!", FlashMessage.Status.SUCCESS));
        }
        return "redirect:/categories";
    }
}
