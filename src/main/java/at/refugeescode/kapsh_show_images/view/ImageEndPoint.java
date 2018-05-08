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
import java.util.List;


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

}
