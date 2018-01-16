package org.elsys.ip.rest.service;

import org.elsys.ip.rest.model.Camera;

import org.elsys.ip.rest.repository.CameraRepository;

import javax.ws.rs.core.MultivaluedMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class CameraService {
    private CameraRepository cameraRepository = new CameraRepository();
    private static AtomicLong idCounter = new AtomicLong();

    public List<Camera> getCameraList() {
        return cameraRepository.getCameraList();
    }

    public Camera getCameraById(Integer id) {
        return cameraRepository.getCameraById(id).orElse(null);
    }

    public Camera saveCamera(Camera camera) {
        camera.setId(idCounter.incrementAndGet());
        return cameraRepository.saveCamera(camera);
    }

    public Camera updateCamera(Integer id,Camera camera) {
        return cameraRepository.updateCamera(id, camera);
    }

    public void deleteCamera(Integer id) {
        cameraRepository.deleteCamera(id);
    }

    public List<Camera> getCameraByFields(MultivaluedMap<String,String> params) {
        return cameraRepository.getCameraByFields(params);
    }

    public List<Camera> findByMultipleIds(HashSet<Long> ids) {
        return cameraRepository.getByIds(ids);
    }

    public List<Camera> saveMultipleCameras(List<Camera> cameras) {
        for (Camera camera : cameras) {
            camera.setId(idCounter.incrementAndGet());
        }

        return cameraRepository.saveMultipleCameras(cameras);
    }

    public void writeCSVFile() throws Exception{
        cameraRepository.writeCSVFile();
    }
}
