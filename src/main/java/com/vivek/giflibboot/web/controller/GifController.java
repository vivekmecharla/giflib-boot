package com.vivek.giflibboot.web.controller;

import com.vivek.giflibboot.data.CategoryRepository;
import com.vivek.giflibboot.data.GifRepository;
import com.vivek.giflibboot.model.Gif;
import com.vivek.giflibboot.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        // TODO: Get all gifs
        List<Gif> gifs = new ArrayList<>();

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
        // TODO: Get list of all GIFs marked as favorite
        List<Gif> faves = new ArrayList<>();

        model.addAttribute("gifs", faves);
        model.addAttribute("username", "Chris Ramacciotti"); // Static username
        return "gif/favorites";
    }

    // Upload a new GIF
    @RequestMapping(value = "/gifs", method = RequestMethod.POST)
    public String addGif(Gif gif, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            gif.setBytes(file.getBytes());
        } catch (IOException e) {
            System.err.println("Unable to get byte array from uploaded file");
        }
        gifRepository.save(gif);

        redirectAttributes.addFlashAttribute("flash",
                new FlashMessage("Gif successfully uploaded", FlashMessage.Status.SUCCESS));
        return String.format("redirect:/gifs/%s", gif.getId());
    }

    // Form for uploading a new GIF
    @RequestMapping("/upload")
    public String formNewGif(Model model) {
        model.addAttribute("gif", new Gif());
        model.addAttribute("categories", categoryRepository.findAll());
        // TODO: Add model attributes needed for new GIF upload form

        return "gif/form";
    }

    // Form for editing an existing GIF
    @RequestMapping(value = "/gifs/{dgifI}/edit")
    public String formEditGif(@PathVariable Long gifId, Model model) {
        // TODO: Add model attributes needed for edit form

        return "gif/form";
    }

    // Update an existing GIF
    @RequestMapping(value = "/gifs/{gifId}", method = RequestMethod.POST)
    public String updateGif() {
        // TODO: Update GIF if data is valid

        // TODO: Redirect browser to updated GIF's detail view
        return null;
    }

    // Delete an existing GIF
    @RequestMapping(value = "/gifs/{gifId}/delete", method = RequestMethod.POST)
    public String deleteGif(@PathVariable Long gifId) {
        // TODO: Delete the GIF whose id is gifId

        // TODO: Redirect to app root
        return null;
    }

    // Mark/unmark an existing GIF as a favorite
    @RequestMapping(value = "/gifs/{gifId}/favorite", method = RequestMethod.POST)
    public String toggleFavorite(@PathVariable Long gifId) {
        // TODO: With GIF whose id is gifId, toggle the favorite field

        // TODO: Redirect to GIF's detail view
        return null;
    }

    // Search results
    @RequestMapping("/search")
    public String searchResults(@RequestParam String q, Model model) {
        // TODO: Get list of GIFs whose description contains value specified by q
        List<Gif> gifs = new ArrayList<>();

        model.addAttribute("gifs", gifs);
        return "gif/index";
    }
}