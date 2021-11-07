package com.cv.indexation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import com.cv.indexation.document.CurriculumVitae;
import com.cv.indexation.service.CurriculumVitaeService;
import com.cv.indexation.search.SearchRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

@RestController
@RequestMapping("/api/cv")
public class CurriculumVitaeController {
    private final CurriculumVitaeService service;
    
    @Autowired
    public CurriculumVitaeController(CurriculumVitaeService service) {
        this.service = service;
    }

    @PostMapping
    public void index(@RequestBody final CurriculumVitae curriculumVitae) {
        service.index(curriculumVitae);
    }

    @GetMapping("/{id}")
    public CurriculumVitae getById(@PathVariable final String id) {
        return service.getById(id);
    }

    @PostMapping("/search")
    public List<CurriculumVitae> search(@RequestBody final SearchRequestDTO dto) {
        return service.search(dto);
    }
    

     private final Logger logger = LoggerFactory.getLogger(CurriculumVitaeController.class);
    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile uploadfile) {

        logger.debug("Single file upload!");

        if (uploadfile.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }
        try {
        	indexFile(Arrays.asList(uploadfile));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded - " +
                uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);

    }
    
    //save file
    private void indexFile(List<MultipartFile> files) throws IOException{
    	CurriculumVitae curriculumVitae = new CurriculumVitae();
        File file = multipartToFile(files.get(0), files.get(0).getOriginalFilename());
        if (isPDF(file)) {
        	PdfReader reader = new PdfReader(file.getAbsolutePath());
        	String str=PdfTextExtractor.getTextFromPage(reader, 1);
        	curriculumVitae.setContent(str);
        } else {
        	FileInputStream fis = new FileInputStream(file);
    		XWPFDocument doc = new XWPFDocument(fis);
    		XWPFWordExtractor ex = new XWPFWordExtractor(doc);
    		String text = ex.getText();
        	curriculumVitae.setContent(text);
        }
        curriculumVitae.setNameFile(file.getName());
    	service.index(curriculumVitae);
    }
    
    public File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
        multipart.transferTo(convFile);
        return convFile;
    }
    
    public boolean isPDF(File file) throws FileNotFoundException{
        Scanner input = new Scanner(new FileReader(file));
        while (input.hasNextLine()) {
            final String checkline = input.nextLine();
            if(checkline.contains("%PDF-")) { 
                return true;
            }  
        }
        return false;
    }   
}