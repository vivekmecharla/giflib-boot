package com.vivek.giflibboot.web.controller;

import com.vivek.giflibboot.data.CategoryRepository;
import com.vivek.giflibboot.data.GifRepository;
import com.vivek.giflibboot.model.Gif;
import com.vivek.giflibboot.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GifController {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    GifRepository gifRepository;
    // Home page - index of all GIFs
    @RequestMapping("/")
    public String listGifs(Model model) {
        //TODO fix search functionality
        List<Gif> gifs = (List<Gif>) gifRepository.findAll();
        model.addAttribute("gifs", gifs);
        return "gif/index";
    }

    // Single GIF page
    @RequestMapping("/gifs/{gifId}")
    public String gifDetails(@PathVariable Long gifId, Model model) {
        Gif gif = gifRepository.findById(gifId);
        model.addAttribute("gif", gif);
        return "gif/details";
    }

    // GIF image data
    @RequestMapping("/gifs/{gifId}.gif")
    @ResponseBody
    public byte[] gifImage(@PathVariable Long gifId) {
        return gifRepository.findById(gifId).getBytes();
    }

    // Favorites - index of all GIFs marked favorite
    @RequestMapping("/favorites")
    public String favorites(Model model) {
        List<Gif> faves = gifRepository.findByFavorite(true);
        model.addAttribute("gifs", faves);
        model.addAttribute("username", "Chris Ramacciotti"); // Static username
        return "gif/favorites";
    }

    // Upload a new GIF
    @RequestMapping(value = "/gifs", method = RequestMethod.POST)
    public String addGif(@Valid Gif gif, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes, BindingResult result) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category", result);
            redirectAttributes.addFlashAttribute("gif", gif);
            return String.format("redirect:/upload", gif.getId());
        }
        try {
            gif.setBytes(file.getBytes());
        } catch (IOException e) {
            System.err.println("Unable to get byte array from uploaded file");
        }
        gifRepository.save(gif);

        redirectAttributes.addFlashAttribute("flash",
                new FlashMessage("Gif successfully uploaded!", FlashMessage.Status.SUCCESS));
        return String.format("redirect:/gifs/%s", gif.getId());
    }

    // Form for uploading a new GIF
    @RequestMapping("/upload")
    public String formNewGif(Model model) {
        model.addAttribute("gif", new Gif());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("heading", "Upload");
        model.addAttribute("submit", "Upload");
        model.addAttribute("action", "/gifs");
        return "gif/form";
    }

    // Form for editing an existing GIF
    @RequestMapping(value = "/gifs/{gifId}/edit")
    public String formEditGif(@PathVariable Long gifId, Model model) {
        model.addAttribute("gif", gifRepository.findById(gifId));
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("heading", "Edit");
        model.addAttribute("submit", "Save");
        model.addAttribute("action", "/gifs/{gifId}}");
        return "gif/form";
    }

    // Update an existing GIF
    @RequestMapping(value = "/gifs/{gifId}", method = RequestMethod.POST)
    public String updateGif(@Valid Gif gif, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes, BindingResult result) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category", result);
            redirectAttributes.addFlashAttribute("gif", gif);
            return String.format("redirect:/gif/%s/edit", gif.getId());
        }
        try {
            gif.setBytes(file.getBytes());
        } catch (IOException e) {
            System.err.println("Unable to get byte array from uploaded file");
        }
        gifRepository.save(gif);

        redirectAttributes.addFlashAttribute("flash",
                new FlashMessage("Gif successfully updated!", FlashMessage.Status.SUCCESS));
        return String.format("redirect:/gifs/%s", gif.getId());
    }

    // Delete an existing GIF
    @RequestMapping(value = "/gifs/{gifId}/delete", method = RequestMethod.POST)
    public String deleteGif(@PathVariable Long gifId) {
        gifRepository.delete(gifRepository.findById(gifId));
        return "redirect:/";
    }

    // Mark/unmark an existing GIF as a favorite
    @RequestMapping(value = "/gifs/{gifId}/favorite", method = RequestMethod.POST)
    public String toggleFavorite(@PathVariable Long gifId) {
        Gif gif = gifRepository.findById(gifId);
        gif.setFavorite(!gif.isFavorite());
        gifRepository.save(gif);
        return String.format("redirect:/gifs/%s", gif.getId());
    }

    // Search results
    @RequestMapping("/search")
    public String searchResults(@RequestParam String q, Model model) {
        List<Gif> gifs = new ArrayList<>();
        for (Gif gif : gifRepository.findAll()
                ) {
            if (gif.getDescription().contains(q))
                gifs.add(gif);
        }
        model.addAttribute("gifs", gifs);
        return "redirect:/";
    }
}