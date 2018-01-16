package org.elsys.ip.rest.resource;

import org.elsys.ip.rest.model.Camera;
import org.elsys.ip.rest.service.CameraService;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.File;
import java.util.HashSet;
import java.util.List;

@Path("camera")
public class CameraResource {
  private CameraService cameraService = new CameraService();

  @GET
  @Path("all")
  @Produces("application/json")
  public List<Camera> getCameras() {
    return cameraService.getCameraList();
  }

  @GET
  @Path("/{id}")
  @Produces("application/json")
  public Camera getCamera(@PathParam("id") Integer id) {
    return cameraService.getCameraById(id);
  }

  @GET
  @Path("/searchByField")
  @Produces("application/json")
  public List<Camera> searchByFields(@Context UriInfo uriInfo) {
      MultivaluedMap<String,String> params = uriInfo.getQueryParameters();
      return cameraService.getCameraByFields(params);
  }

  @GET
  @Produces("application/json")
  public List<Camera> getByMultipleIds(@QueryParam("id") final List<Long> listOfIds) {
      return cameraService.findByMultipleIds(new HashSet<Long>(listOfIds));
  }

  @GET
  @Path("/download")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public Response downloadTest() throws Exception {
    cameraService.writeCSVFile();
    File file = new File("E:\\temp\\data.csv");
    return Response
            .ok(file)
            .header("Content-Disposition", "attachment; filename=" + file.getName())
            .build();
  }

  @POST
  @Consumes("application/json")
  @Produces("application/json")
  public Camera saveCamera(Camera camera) {
    return cameraService.saveCamera(camera);
  }

  @POST
  @Path("multiple")
  @Consumes("application/json")
  @Produces("application/json")
  public List<Camera> saveMultipleCameras(List<Camera> cameras) {
      return cameraService.saveMultipleCameras(cameras);
  }

  @PUT
  @Path("/{id}")
  @Consumes("application/json")
  @Produces("application/json")
  public Camera updateTest(@PathParam("id") Integer id, Camera camera) {
    return cameraService.updateCamera(id,camera);
  }

  @DELETE
  @Path("/{id}")
  public void deleteCamera(@PathParam("id") Integer id) {
    cameraService.deleteCamera(id);
  }

}
