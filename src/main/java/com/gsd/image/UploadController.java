package com.gsd.image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.stream.Stream;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UploadController {
	
	@GetMapping("/")
	public String showImages(RedirectAttributes redirect) {
		AWSS3Images awsImages = new AWSS3Images();
		redirect.addFlashAttribute("images",awsImages.readImage());
		System.out.println(redirect.getFlashAttributes());
		return "redirect:allImage";
	}
	
	@GetMapping("/allImage")
	public String allImage() {
		return "allImages";
	}
	
	@GetMapping("/selectFile")
	public String upload() {
		return "upload";
	}
	
	@PostMapping("/upload")
	public String singleFileUpload(@RequestParam("file")MultipartFile file, RedirectAttributes redirect) {
		if(file.isEmpty()) {
			redirect.addFlashAttribute("message","Please select a file to upload.");
			return "redirect:uploadStatus";
		}
		try {
			AWSS3Images cBucket = new AWSS3Images();
			System.out.println("Sending to AWS Bucket Object");
			cBucket.writeImage(convertMultiPartToFile(file));
			System.out.println("sent");
			redirect.addFlashAttribute("message","Files have been upload successfully '"+file.getOriginalFilename()+"'!");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:/uploadStatus";
	}
	
	private File convertMultiPartToFile(MultipartFile file ) throws IOException{
		System.out.println("Converting");
		File convFile = new File( file.getOriginalFilename() );
        FileOutputStream fos = new FileOutputStream( convFile );
        fos.write( file.getBytes() );
        fos.close();
        System.out.println("Converted");
        return convFile;
    }
	
	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}
}
