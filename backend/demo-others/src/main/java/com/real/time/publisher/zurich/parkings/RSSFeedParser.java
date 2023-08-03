package com.real.time.publisher.zurich.parkings;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class RSSFeedParser {

    private URL url;

    public RSSFeedParser() {
        try {
            this.url = new URL("https://www.pls-zh.ch/plsFeed/rss");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public ParkingsFeed readFeed() {
        ParkingsFeed parkingsFeed = null;
        try{
            boolean isFeedHeader = true;

            String description = "";
            String title = "";
            String link = "";
            String copyright = "";

            XMLInputFactory inputFactory = XMLInputFactory.newInstance();

            InputStream in = read();

            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

            while(eventReader.hasNext()){
                XMLEvent event = eventReader.nextEvent();
                if(event.isStartElement()){
                    String localPath = event.asStartElement().getName().getLocalPart();
                    switch (localPath){
                        case "item":
                            if(isFeedHeader){
                                isFeedHeader = false;
                                parkingsFeed = new ParkingsFeed(title, link, description, copyright);
                            }
                            event = eventReader.nextEvent();
                            break;
                        case "title":
                            title = getCharacterData(event, eventReader);
                            break;
                        case "description":
                            description = getCharacterData(event, eventReader);
                            break;
                        case "link":
                            link = getCharacterData(event, eventReader);
                            break;
                        case "copyright":
                            copyright = getCharacterData(event, eventReader);
                            break;
                    }
                } else if (event.isEndElement()){
                    if(event.asEndElement().getName().getLocalPart().equals("item")){
                        ParkingInfo parkingInfo = new ParkingInfo(title, link, description);
                        parkingsFeed.getEntries().add(parkingInfo);
                        event = eventReader.nextEvent();
                    }
                }
            }

        } catch (XMLStreamException e){
            e.printStackTrace();
        }
        return parkingsFeed;
    }

    private String getCharacterData(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException{
        String result = "";
        event = eventReader.nextEvent();
        if(event instanceof Characters){
            result = event.asCharacters().getData();
        }
        return result;
    }

    private InputStream read() {
        try{
            return url.openStream();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
