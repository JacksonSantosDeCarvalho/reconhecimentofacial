
import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.EigenFaceRecognizer;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.opencv.core.CvType;
import org.opencv.imgcodecs.Imgcodecs;




/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Jk-Sa
 */
public class Treinamento {

    public static void main(String[] args) {
        //pegar a pasta que contem as fotos

        File pastaFotos = new File("src/main/java/fotos");

//um filtro para permitir apenas alguns formatos de arquivo de imagens
        FilenameFilter filtroFotos = new FilenameFilter() {

            @Override

            public boolean accept(File dir, String name) {

                return name.endsWith(".jpg") || name.endsWith(".gif")
                        || name.endsWith(".png");

            }

        };

//pegando os arquivos das fotos
        File[] arquivos = pastaFotos.listFiles(filtroFotos);

//vetor de imagens com o número de posições iguais ao número de arquivos
        MatVector fotos = new MatVector(arquivos.length);
        //os rotulos/classes da IA de cada foto.

// numero de linhas, colunas, tipo de dado do rotulo
        Mat rotulos = new Mat(arquivos.length, 1, CvType.CV_32SC1);

        IntBuffer rotulosBuffer = rotulos.createBuffer();

        int contador = 0;

        for (File imagem : arquivos) {

//Ler a imagem em escala de cinza
            Mat foto = opencv_imgcodecs.imread(imagem.getAbsolutePath(),
                    Imgcodecs.IMREAD_GRAYSCALE);

//pegando a classe/rotulo de cada foto (de quem é o dono da foto)
            String id = imagem.getName().split("_")[0];

            int classe = Integer.parseInt(id);

            //redimenciono
            opencv_imgproc.resize(foto, foto, new Size(160, 160));

//adiciono a foto já redimencionada no vetor de fotos
            fotos.put(contador, foto);

//adiciono ao buffer o contador da foto e depois a sua classe.
//Percaba que aqui podemos ter mais de uma pessoa, ou seja,varios rotulos/classes 
            rotulosBuffer.put(contador, classe);

            contador++;



        }
        
        
            FaceRecognizer eigenfaces = EigenFaceRecognizer.create();

            FaceRecognizer fisherfaces = FisherFaceRecognizer.create();

            FaceRecognizer lbphfaces = LBPHFaceRecognizer.create();
        
            eigenfaces.train(fotos, rotulos);

            eigenfaces.save("src/main/java/recursos/eigen.yml");

            fisherfaces.train(fotos, rotulos);

            fisherfaces.save("src/main/java/recursos/fisher.yml");

            lbphfaces.train(fotos, rotulos);

            lbphfaces.save("src/main/java/recursos/lbph.yml");

    }
}
