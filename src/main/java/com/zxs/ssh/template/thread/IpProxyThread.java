package com.zxs.ssh.template.thread;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

public class IpProxyThread implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(IpProxyThread.class);
    public static LinkedBlockingQueue<String> ipProxyQueue = new LinkedBlockingQueue<>();
    private boolean isBreakdown = false;

    @Override
    public void run() {
        while(!isBreakdown){
            try{
                String url = "http://www.xicidaili.com/nn/";
                Document document = Jsoup.connect(url).get();
                Elements elements = document.select("tr");
                for (int i = 1; i < elements.size(); i++) {
                    String ip = elements.get(i).select("td").get(1).html() + ":"
                            + elements.get(i).select("td").get(2).html();
                    ipProxyQueue.add(ip);
                    System.out.println(ip);
                }
                if(ipProxyQueue.size() > 0){
                    Thread.sleep(60*1000);
                }
            }catch (Exception e){
                logger.info("爬取代理IP异常", e);
                isBreakdown = true;
            }
        }

    }

    public static void main(String[] args) {
        IpProxyThread ipProxyThread = new IpProxyThread();
        new Thread(ipProxyThread).start();
    }
}