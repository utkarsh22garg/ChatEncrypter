/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatEncrypter.Networking;

/**
 *
 * @author MNNIT
 */
public interface WritableGUI {

    public void write(String s);

    public void clear();

    public void connectClient(String ip, int port);

    public void disconnectClient();

    public void showHostInfo(String hostIp, int hostPort);

    public void showClientInfo(String clientIp, int clientPort);
}
