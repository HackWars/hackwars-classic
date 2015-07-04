package GUI;

import javax.sound.sampled.*;
import java.io.*;
import java.net.*;
import javax.sound.midi.*;
import java.util.*;
import util.*;
import java.util.concurrent.Semaphore;

public class Sound implements Runnable{
	private  final String URL = "http://www.team-captin.com/hacker/sounds/";
	private  String tmpDir = System.getProperty("java.io.tmpdir")+"/hackwars/sounds/";
	private  boolean mute=false;
	private  Synthesizer synth = null;
	private  float musicVolume=1.0f;
	private  boolean musicMute=false; 
	private  Thread MyThread=null;
	private boolean running=true;
	private long sleepTime=50;
	private long startTime;
	private long endTime;
	private  final Semaphore available = new Semaphore(1, true);//Make it thread safe.
	private  ArrayList Work=new ArrayList();
	private HashMap loaded = new HashMap();
	
	public  void startSound(){
		try{
			File CF = new File(tmpDir);
			if(!CF.exists()){
				CF.mkdirs();
			}
			//System.out.println("Starting Sound");
			Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
			int val=0;
			for(int i=0;i<mixerInfos.length;i++){
				if(mixerInfos[i].getName().equals("Java Sound Audio Engine")){
					val=i;
					break;
				}
				//System.out.println(mixerInfos[i].getName());
			}
			
			Mixer mixer = AudioSystem.getMixer(mixerInfos[val]);
			Line.Info[] lineInfos = mixer.getTargetLineInfo();
			for(int i=0;i<lineInfos.length;i++){
				Line line = AudioSystem.getLine(lineInfos[i]);
				line.open();
			}
			//AudioSystem.getLine(line);
			Soundbank soundbank=null;
			try{
				soundbank = MidiSystem.getSoundbank(new URL("http://www.team-captin.com/hacker/soundbank-min.gm"));
			}catch(Exception e){e.printStackTrace();}
			if(soundbank!=null){
				/*synth = MidiSystem.getSynthesizer();
				synth.open();
				Sequencer sequencer = MidiSystem.getSequencer();
				sequencer.open();
				boolean loaded = synth.loadAllInstruments(soundbank);
				Sequence seq = new Sequence(Sequence.SMPTE_30DROP,1);
				Track track1 = seq.createTrack();
				
				ShortMessage message = new ShortMessage();
				message.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 20, 0);
				MidiEvent event = new MidiEvent(message,0l);
				track1.add(event);
				message = new ShortMessage();
				message.setMessage(ShortMessage.NOTE_ON, 0, 60, 93);
				event = new MidiEvent(message,0);
				track1.add(event);
				message = new ShortMessage();
				message.setMessage(ShortMessage.NOTE_OFF, 0, 60, 93);
				event = new MidiEvent(message,50);
				track1.add(event);
				
				sequencer.setSequence(seq);
				sequencer.start();
				
				while(sequencer.isRunning()){
				}
				sequencer.close();
				synth.close();*/
				/*channels[0].noteOn(60, 93);
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e)
				{
				}
				channels[0].noteOff(60);*/
				//System.exit(1);
			}
			else{System.out.println("Soundbank is null");}
			
		}catch(Exception e){e.printStackTrace();}
		
		MyThread=new Thread(this);
		MyThread.start();
	}
	
	public  void play(int index){
			try{
				available.acquire();
				Work.add(new Integer(index));
				available.release();
			}catch(Exception e){
			
			}		
	}
	
	public  void setEffectsMute(boolean mute){
		this.mute=mute;
	}
	
	public  void setEffectsVolume(float volume){
		
	}
	
	public void run(){
		while(running){
			long startTime=Time.getInstance().getCurrentTime();
			int index=0;
			try{
				available.acquire();
				if(Work.size()>0)
					index=(Integer)Work.remove(0);
				available.release();
			}catch(Exception e){
			
			}
			
			try{
				
				if(index>0){
					if(!mute){
						try{
							File CF=null;
							if(loaded.get(new Integer(index))==null){
								Object[] object  = new Object[]{(Integer)index};
								String file = (String)XMLRPCCall.execute("http://www.hackwars.net/xmlrpc/sounds.php","getFile",object);
								String check = tmpDir+file;
									CF = new File(check);
									if(!CF.exists()){
										 
										InputStream in = new URL(URL+file).openStream();
										FileOutputStream FOS=new FileOutputStream(tmpDir+file);
										int i=0;
										byte buffer[]=new byte[512];
										while((i=in.read(buffer))>0)
											FOS.write(buffer,0,i);
										FOS.close();
									}
									loaded.put(new Integer(index),check);
							}
							else{
								CF = new File((String)loaded.get(new Integer(index)));
							}
							//System.out.println("Playing "+file);
							//URL f = new URL(file);
							AudioInputStream AIS = AudioSystem.getAudioInputStream(CF);
							Clip c = AudioSystem.getClip();
							c.open(AIS);
							c.start();
							
						}catch(Exception e){}
					}
				}
				
				endTime=Time.getInstance().getCurrentTime();
				if(sleepTime-(endTime-startTime)>0){
					MyThread.sleep(sleepTime-(endTime-startTime));
				}
			
			}catch(Exception e){};
		}
	}
}
