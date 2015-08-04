package export;

import coolor.ImageModel;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public interface XlsCRUD {
    public File createXlsFile(String name, List<ImageModel> dataToFile);

    public List<ImageModel> readCompaniesListFromXlsFile(FileInputStream fis);

    public boolean updateXlsFile(File existFile, List<ImageModel> newData);

    public boolean deleteXlsFile(File file);
}
