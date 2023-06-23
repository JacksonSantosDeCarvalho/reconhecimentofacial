
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Jk-Sa
 */
public class Captura {

    public static void main(String[] args) throws InterruptedException {

        try {
            KeyEvent tecla = null;

            OpenCVFrameConverter.ToMat convertMat = new OpenCVFrameConverter.ToMat();

            OpenCVFrameGrabber camera = new OpenCVFrameGrabber(0);

            camera.start();

            CascadeClassifier detectorFace = new CascadeClassifier("src/main/java/recursos/haarcascade-frontalface.xml");
            Mat imagemColorida = new Mat();

            int numeroAmostrarTreinamento = 20;

            int contador = 1;

            int id = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite o id da pessoa:"));

            CanvasFrame cFrame = new CanvasFrame("Nossa câmera", CanvasFrame.getDefaultGamma() / camera.getGamma());

            Frame frameCapturado = null;

            while ((frameCapturado = camera.grab()) != null) {

                imagemColorida = convertMat.convert(frameCapturado);

                Mat imagemCinza = new Mat();

                opencv_imgproc.cvtColor(imagemColorida, imagemCinza, opencv_imgproc.COLOR_BGRA2GRAY);

                RectVector facesDetectadas = new RectVector();

                detectorFace.detectMultiScale(imagemCinza, facesDetectadas, 1.1, 1, 0, new Size(150, 150), new Size(500, 500));

                for (int i = 0; i < facesDetectadas.size(); i++) {

                    Rect dadosFace = facesDetectadas.get(0);

                    opencv_imgproc.rectangle(imagemColorida, dadosFace, new Scalar(0, 255, 0, 0));

                    Mat faceCapturada = new Mat(imagemCinza, dadosFace);

                    resize(faceCapturada, faceCapturada, new Size(160, 160));

                    if (tecla == null) {

                        tecla = cFrame.waitKey(5);

                    }

                    if (tecla != null) {

                        if (tecla.getKeyChar() == 'f') {

                            opencv_imgcodecs.imwrite("src/main/java/fotos/" + id + "_" + contador + ".jpg", faceCapturada);

                            System.out.println("foto " + contador);

                            contador++;

                        }

                    }

                    

                }
                if (contador > 25) {

                    break;

                }
                if (cFrame.isVisible()) {

                    cFrame.showImage(frameCapturado);

                }

            }

            cFrame.dispose();

            camera.stop();

        } catch (FrameGrabber.Exception ex) {

            Logger.getLogger(Captura.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
}
