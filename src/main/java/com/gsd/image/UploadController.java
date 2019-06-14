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
//	private static String UPLOAD_FOLDER = "/Users/user/eclipse/";
//	ArrayList<ImageData> allPath;
	//ArrayList<byte[]> allImage;
	
//	@GetMapping("/")
//	@ResponseBody
//	public ArrayList<ImageData> index() {
////		public ArrayList<String> index(RedirectAttributes redirect) {
//		Path path = Paths.get(UPLOAD_FOLDER);
//		allPath = new ArrayList<ImageData>();
//		//allImage = new ArrayList<byte[]>();
//		Stream<Path> stream = null;
//			try {
//				stream = Files.list(path);
//				stream.forEach(a -> {
//					if(a.toString().endsWith(".jpg"))
//						//try {
//							allPath.add(new ImageData(a.toString()));
//							//byte data[] = Files.readAllBytes(a);
//							//allImage.add(data);
//			//				allImage.add(Files.readAllBytes(a));
//						//} catch (IOException e) {
//						//	e.printStackTrace();
//						//}
//						//allPath.add(a.toString());
//					//redirect.addFlashAttribute("images", allImage);
//					//redirect.addFlashAttribute("images",allPath);
//					//System.out.println(redirect.getFlashAttributes());
//				});
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			//return allPath;
////		return "redirect:allImage";
//			return allPath;
//	}
	
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
//			byte bytes[] = file.getBytes();
//			Path path = Paths.get(UPLOAD_FOLDER, file.getOriginalFilename());
			//Files.write(path, bytes);
			//Amazon AWS for file uploading below 3 lines
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
