package model;

import utils.ApiService;
import utils.VariableAPI;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Port;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.result.WordResult;
/**
 *
 * @author TANJONA fetrasoa
 */
public class SpeechRecognizerMain {  
    private LiveSpeechRecognizer recognizer;  
    private Logger logger = Logger.getLogger(getClass().getName());  
    private String speechRecognitionResult;
    private boolean ignoreSpeechRecognitionResults = false;   
    private boolean speechRecognizerThreadRunning = false;   
    private boolean resourcesThreadRunning;
    private ExecutorService eventsExecutorService = Executors.newFixedThreadPool(2);
   
    public SpeechRecognizerMain() {
         logger.log(Level.INFO, "chargment des materiel...\n");
        Configuration configuration = new Configuration();      
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setGrammarPath("resource:/grammars");
        configuration.setGrammarName("grammar");
        configuration.setUseGrammar(true);
        
        try {
            recognizer = new LiveSpeechRecognizer(configuration);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        startResourcesThread();    
        startSpeechRecognition();
    }

   
    public synchronized void startSpeechRecognition() {      
        if (speechRecognizerThreadRunning) {
            logger.log(Level.INFO, "reconnaissance vocal est encore lancer...\n");
        } else 
        {
            eventsExecutorService.submit(() -> {
                speechRecognizerThreadRunning = true;
                ignoreSpeechRecognitionResults = false;              
                recognizer.startRecognition(true);
		
                logger.log(Level.INFO, "on peut parler prêt à ecouter...\n");
                try {
                    while (speechRecognizerThreadRunning) {                        
                        SpeechResult speechResult = recognizer.getResult();                     
                        if (!ignoreSpeechRecognitionResults) {                            
                            if (speechResult == null) {
                                logger.log(Level.INFO, "signal vocal inconnue.\n");
                            } else {                               
                                speechRecognitionResult = speechResult.getHypothesis();                            
                                System.out.println("dit: [" + speechRecognitionResult + "]\n");                                
                                makeDecision(speechRecognitionResult, speechResult.getWords());                            }
                        } else {
                            logger.log(Level.INFO, "Ingoring Speech Recognition Results...");
                        }

                    }
                } catch (Exception ex) {
                    logger.log(Level.WARNING, null, ex);
                    speechRecognizerThreadRunning = false;
                }
                logger.log(Level.INFO, "SpeechThread has exited...");

            });
        }
    }

    public synchronized void stopIgnoreSpeechRecognitionResults() {
        ignoreSpeechRecognitionResults = false;
    }

    public synchronized void ignoreSpeechRecognitionResults() {
        ignoreSpeechRecognitionResults = true;

    }

   
    public void startResourcesThread() {  
        if (resourcesThreadRunning) {
            logger.log(Level.INFO, "Resources Threadencours...\n");
        } else 
        {
            eventsExecutorService.submit(() -> {
                try {       
                    resourcesThreadRunning = true;
                    while (true) {
                        if (!AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
                            logger.log(Level.INFO, "Microphone valable.\n");
                        }
                        Thread.sleep(350);
                    }
                } catch (InterruptedException ex) {
                    logger.log(Level.WARNING, null, ex);
                    resourcesThreadRunning = false;
                }
            });
        }
    }
/**
 * 
 * @param speech
 * @param speechWords 
 */
    public void makeDecision(String speech, List<WordResult> speechWords) {
        System.out.println(speech);
        if (speech.equals("on")) {
            ApiService.postAPI(VariableAPI.switchDeviceUrl, VariableAPI.onDevice);
        }
        if (speech.equals("off")) {
            ApiService.postAPI(VariableAPI.switchDeviceUrl, VariableAPI.offDevice); 
        }
    }

    public boolean getIgnoreSpeechRecognitionResults() {
        return ignoreSpeechRecognitionResults;
    }

    public boolean getSpeechRecognizerThreadRunning() {
        return speechRecognizerThreadRunning;
    }

    /**
     * Main Method
     *
     * @param args
     */
    public static void main(String[] args) {
        new SpeechRecognizerMain();
    }
}
