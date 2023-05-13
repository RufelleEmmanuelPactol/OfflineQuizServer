package onBoard.network.client;

import onBoard.network.networkUtils.NetworkGlobals;

public class ClientMain {
    public static void main(String[] args) throws Throwable {
       NetworkGlobals.createSession("kurapika@leorio.com", "BiscuitKrueger");
       var response = NetworkGlobals.session().getUserInfo();

    }

}
