package export;

import coolor.models.BlankImageModel;
import coolor.models.ImageModel;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public interface XlsCRUD {
    public File createXlsFile(String name, List<BlankImageModel> dataToFile);

    public List<BlankImageModel> readCompaniesListFromXlsFile(FileInputStream fis);

    public boolean updateXlsFile(File existFile, List<BlankImageModel> newData);

    public boolean deleteXlsFile(File file);
}
