package com.refpro.server.DBhandlers;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.refpro.server.DTOs.RefereeDTO;
import com.refpro.server.exception.AbstractRestException;
import com.refpro.server.exception.InvalidInputException;
import com.refpro.server.exception.RefereeNotFoundException;
import com.refpro.server.models.Referee;
import com.refpro.server.repositories.RefereeRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Component
public class RefereeHandler {
    @Autowired
    private RefereeRepository refereeRepository;

    public static final String REFEREE_PICTURES_BUCKET_NAME = "refereePictures";

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<RefereeDTO> getAllReferees() {
        return convertToDTO(refereeRepository.findAll());
    }

    public RefereeDTO getRefereeByID(String refereeID) throws RefereeNotFoundException {
        Referee entry = refereeRepository.findOne(refereeID);

        RefereeDTO result = new RefereeDTO();

        if (entry == null)
            throw new RefereeNotFoundException("Referee not found.");

        result.setFullName(entry.getFullName());
        result.setBirthday(entry.getBirthday());
        result.setWeight(entry.getWeight());
        result.setHeight(entry.getHeight());
        result.setExperience(entry.getExperience());

        result.setAvaregeMark(entry.getMarks().stream().mapToDouble(d -> d).average().orElse(0));

        return result;
    }

    private List<RefereeDTO> convertToDTO(List<Referee> referees) {
        List<RefereeDTO> result = new ArrayList<>();

        for (Referee referee : referees) {
            OptionalDouble marksResult;

            RefereeDTO entry = new RefereeDTO();

            entry.setFullName(referee.getFullName());
            entry.setHeight(referee.getHeight());
            entry.setWeight(referee.getWeight());
            entry.setExperience(referee.getExperience());
            entry.setBirthday(referee.getBirthday());

            marksResult = referee.getMarks().stream().mapToDouble(d -> d).average();

            entry.setAvaregeMark(marksResult.orElse(0));

            result.add(entry);
        }

        return result;
    }

    public String createReferee(RefereeDTO refereeDTO) {
        Referee newEntry = new Referee();

        newEntry.setFullName(refereeDTO.getFullName());
        newEntry.setBirthday(refereeDTO.getBirthday());
        newEntry.setHeight(refereeDTO.getHeight());
        newEntry.setWeight(refereeDTO.getWeight());
        newEntry.setExperience(refereeDTO.getExperience());

        return refereeRepository.save(newEntry).getId();
    }

    public void deleteReferee(String id) throws RefereeNotFoundException {
        Referee entry = refereeRepository.findOne(id);

        if (entry == null)
            throw new RefereeNotFoundException("Referee not found.");

        refereeRepository.delete(entry);
    }

    public void addMarkToReferee(String id,Double mark) throws RefereeNotFoundException {
        Referee entry = refereeRepository.findOne(id);

        if (entry == null)
            throw new RefereeNotFoundException("Referee not found.");

        entry.addMark(mark);

        refereeRepository.save(entry);
    }

    public void addRefereePicture(String refereeID, MultipartFile file) throws IOException, AbstractRestException {

        if(StringUtils.isBlank(refereeID)){
            throw new InvalidInputException("Player id can not be null or empty!");
        }

        Referee referee = refereeRepository.findOne(refereeID);

        if (referee == null) {
            throw new RefereeNotFoundException("Referee not found.");
        }
        GridFS gfsPhoto = new GridFS(mongoTemplate.getDb(), REFEREE_PICTURES_BUCKET_NAME);
        GridFSInputFile gfsFile = gfsPhoto.createFile(file.getBytes());
        gfsFile.setChunkSize(4096*1024);
        ObjectId id = new ObjectId();
        gfsFile.setFilename(id.toString());
        gfsFile.setId(id);
        gfsFile.save();

        referee.setPictureID(gfsFile.getId().toString());
        refereeRepository.save(referee);

    }

    public ByteArrayResource getRefereePicture(String refereeID) throws AbstractRestException,IOException{
        if(StringUtils.isBlank(refereeID)){
            throw new InvalidInputException("Player id can not be null or empty!");
        }

        Referee referee = refereeRepository.findOne(refereeID);

        if (referee == null) {
            throw new RefereeNotFoundException("Referee not found.");
        }

        String pictureId = referee.getPictureID();

        if(referee.getPictureID()!=null){
            GridFS gfsPhoto = new GridFS(mongoTemplate.getDb(), REFEREE_PICTURES_BUCKET_NAME);

            List<GridFSDBFile> pics = gfsPhoto.find(pictureId);
            if(pics.size()>0){
                byte[] picAsBytes = IOUtils.toByteArray(pics.get(0).getInputStream());
                ByteArrayResource resource = new ByteArrayResource(picAsBytes );
                return resource;
            }

        }
        return null;
    }
}
