/*
 * Jicofo, the Jitsi Conference Focus.
 *
 * Copyright @ 2015-Present 8x8, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jitsi.jicofo;

import static org.junit.jupiter.api.Assertions.*;

import mock.xmpp.*;
import org.jitsi.jicofo.xmpp.*;
import org.jitsi.xmpp.extensions.jingle.*;
import org.jivesoftware.smack.packet.*;
import org.junit.jupiter.api.*;
import org.jxmpp.jid.impl.*;
import org.jxmpp.stringprep.*;

public class ConnectionMockTest
{
    private XmppPeer peerA;
    private XmppPeer peerB;
    private XmppPeer peerC;

    @BeforeEach
    public void setup()
    {
        peerA = new XmppPeer("A");
        peerB = new XmppPeer("B");
        peerC = new XmppPeer("C");

        peerA.start();
        peerB.start();
        peerC.start();
    }

    @AfterEach
    public void tearDown()
    {
        peerA.stop();
        peerB.stop();
        peerC.stop();
    }

    @Test
    public void testXmpConnectionIqGet()
            throws InterruptedException, XmppStringprepException
    {
        UtilKt.tryToSendStanza(peerA.getConnection(), getIq("B"));
        UtilKt.tryToSendStanza(peerA.getConnection(), getIq("C"));
        UtilKt.tryToSendStanza(peerB.getConnection(), getIq("A"));

        Thread.sleep(500);

        assertEquals(1, peerA.getIqCount());
        assertEquals(1, peerB.getIqCount());
        assertEquals(1, peerC.getIqCount());

        assertEquals("a", peerA.getIq(0).getTo().toString());
        assertEquals("b", peerB.getIq(0).getTo().toString());
        assertEquals("c", peerC.getIq(0).getTo().toString());
    }

    private JingleIQ getIq(String to)
            throws XmppStringprepException
    {
        JingleIQ jingle = new JingleIQ(JingleAction.SESSION_INFO, "123");
        jingle.setType(IQ.Type.get);
        jingle.setTo(JidCreate.from(to));

        return jingle;
    }
}
