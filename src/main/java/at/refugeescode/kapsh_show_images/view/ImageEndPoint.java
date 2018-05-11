package at.refugeescode.kapsh_show_images.view;

import at.refugeescode.kapsh_show_images.model.Image;
import at.refugeescode.kapsh_show_images.repository.ImageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class ImageEndPoint {

    private static String UPLOADED_FOLDER = "C:\\Users\\foulm\\Projects\\kapsh_show_images\\src\\main\\resources\\static\\images";

    private Image image;
    private ImageRepository imageRepository;


    public ImageEndPoint(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @GetMapping("/")
    String mainPage(){

        return "home";
    }

//    @RequestMapping(method = RequestMethod.POST)
//    public String submitForm(Model model, @Validated Category category, BindingResult result) {
//        model.addAttribute("colour", colour);
//        String returnVal = "successColour";
//        if(result.hasErrors()) {
//            initModelList(model);
//            returnVal = "colour";
//        } else {
//            model.addAttribute("colour", colour);
//        }
//        return returnVal;
//    }
//    @ModelAttribute("categorylist")
//     List<String> initModelList( ) {
//        List<String> categorylist = new ArrayList<String>();
//        categorylist.add("A");
//        categorylist.add("B");
//        categorylist.add("C");
//        return categorylist;
//    }



    @ModelAttribute("image")
    Image addImage(){
        return new Image();
    }

    @PostMapping("/addImage")
    String addImage(Image image, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                File serverFile = new File(UPLOADED_FOLDER+File.separator+ file.getOriginalFilename());
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                image.setName(file.getOriginalFilename());
                this.image = image;
                imageRepository.save(this.image);
                redirectAttributes.addFlashAttribute("flash.message","Successfully Image uploaded");

            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("flash.message","Failed Image to upload");
                return "You failed to upload " + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + " because the file was empty.";
        }
        return "redirect:/";
    }
    @ModelAttribute("images")
    List<Image> getParticipants() {
        return imageRepository.findAll();
    }
    @PostMapping("/choose")
    String choose(Image image){
        Optional<Image> byId = imageRepository.findById(image.getId());
        if (byId.isPresent()){
            image.setId(byId.get().getId());
            image.setName(byId.get().getName());
            image.setImage(byId.get().getImage());
            this.image=image;
            imageRepository.save(this.image);
        }

        return "redirect:/";
    }

}
