package org.elsys.ip.rest.repository;


import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.elsys.ip.rest.model.Camera;

import javax.ws.rs.core.MultivaluedMap;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CameraRepository {
    private static List<Camera> cameraList = new ArrayList<>();


    public List<Camera> getCameraList() {
        return cameraList;
    }

    public Optional<Camera> getCameraById(Integer id) {
        return cameraList.stream().filter(camera -> camera.getId() == id).findFirst();
    }


    public Camera saveCamera(Camera newEntry) {
        cameraList.add(newEntry);
        return newEntry;
    }

    public Camera updateCamera(Integer id, Camera updateEntry) {
        cameraList.set(id - 1, updateEntry);
        return updateEntry;
    }

    public List<Camera> getByIds(Set<Long> ids) {
        return cameraList.stream().filter(
                camera -> ids.contains(camera.getId())
        ).collect(Collectors.<Camera>toList());
    }

    public void deleteCamera(Integer id) {
        cameraList.removeIf(camera -> camera.getId() == id);
    }

    public List<Camera> getCameraByFields(MultivaluedMap<String,String> params) {
        Collection<Predicate<Camera>> listOfPredicates = new ArrayList<Predicate<Camera>>();

        for(String fieldName: params.keySet()){

            Object value = params.get(fieldName).get(0);
            if (fieldName.equals("manufacturer")) {
                listOfPredicates.add(camera -> camera.getManufacturer().equals(value));
            }else if (fieldName.equals("model")) {
                listOfPredicates.add(camera -> camera.getModel().equals(value));
            }else if (fieldName.equals("megapixels")) {
                listOfPredicates.add(camera -> camera.getMegapixels() == (double)value);
            }else if (fieldName.equals("minISO")) {
                listOfPredicates.add(camera -> camera.getMinISO() == (int)value);
            }else if (fieldName.equals("maxISO")) {
                listOfPredicates.add(camera -> camera.getMaxISO() == (int)value);
            }else if (fieldName.equals("shootingSpeed")) {
                listOfPredicates.add(camera -> camera.getShootingSpeed().equals(value));
            }else if (fieldName.equals("videoRecording")) {
                listOfPredicates.add(camera -> camera.getVideoRecording().equals(value));
            }else if (fieldName.equals("afPoints")) {
                listOfPredicates.add(camera -> camera.getAfPoints() == (int)value);
            }else if (fieldName.equals("crossTypeAFPoints")) {
                listOfPredicates.add(camera -> camera.getCrossTypeAFPoints() == (int)value);
            }else if (fieldName.equals("price")) {
                listOfPredicates.add(camera -> camera.getPrice() == (int)value);
            }

        }

        return cameraList.stream().filter(
                camera -> listOfPredicates.stream().allMatch(predicate -> predicate.test(camera))
        ).collect(Collectors.toList());
    }

    public List<Camera> saveMultipleCameras(List<Camera> cameras) {
         cameraList.addAll(cameras);
         return cameras;
    }

    public void writeCSVFile() throws Exception{
        List<Camera> camerasToCSV = new ArrayList<>();

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(Camera.class);

        ObjectWriter myObjectWriter = mapper.writer(schema);
        File tempFile = new File("E:\\temp\\data.csv");
        FileOutputStream tempFileOutputStream = new FileOutputStream(tempFile);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(tempFileOutputStream, 1024);
        OutputStreamWriter writerOutputStream = new OutputStreamWriter(bufferedOutputStream, "UTF-8");

        for (Camera camera : cameraList) {
            Camera tmp = new Camera();
            tmp.setId(camera.getId());
            tmp.setManufacturer(camera.getManufacturer());
            tmp.setModel(camera.getModel());
            tmp.setMegapixels(camera.getMegapixels());
            tmp.setShootingSpeed(camera.getShootingSpeed());
            tmp.setVideoRecording(camera.getVideoRecording());
            tmp.setMinISO(camera.getMinISO());
            tmp.setMaxISO(camera.getMaxISO());
            tmp.setAfPoints(camera.getAfPoints());
            tmp.setCrossTypeAFPoints(camera.getCrossTypeAFPoints());
            tmp.setPrice(camera.getPrice());

            camerasToCSV.add(tmp);
        }

        myObjectWriter.writeValue(writerOutputStream, camerasToCSV);

    }
}
