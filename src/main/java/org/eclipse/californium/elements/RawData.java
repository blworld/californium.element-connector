/*******************************************************************************
 * Copyright (c) 2014 Institute for Pervasive Computing, ETH Zurich and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 * 
 * Contributors:
 *    Matthias Kovatsch - creator and main architect
 *    Martin Lanter - architect and initial implementation
 *    Kai Hudalla (Bosch Software Innovations GmbH) - several additions and improvements
 ******************************************************************************/
package org.eclipse.californium.elements;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.Principal;
import java.util.Arrays;

/**
 * A container object for the data received or sent via a <code>Connector</code>.
 * 
 * The following meta-data is included:
 * <ul>
 * <li>the source/destination IP address</li>
 * <li>the source/destination port</li>
 * <li>a flag indicating whether the message is a multicast message (default is
 * <code>false</code>)</li>
 * </ul>
 * 
 * A message received from a client via the network may also optionally contain the
 * authenticated sender's identity as a <code>java.security.Principal</code> object.
 */
public class RawData {
	
	public enum MessageType {
		EVENT,
		COMMUNICATION
	}
	
	public enum ConnectorEvent {
		NEW_INCOMING_CONNETION,
		NEW_INCOMING_DISCONNECT
	}

	/** The raw message. */
	public final byte[] bytes;
	
	/** The source/destination address. */
	private InetSocketAddress address;
	
	/** Indicates if this message is a multicast message */
	private boolean multicast;
	
	private final Principal senderIdentity;

	private final MessageType messageType;

	private final ConnectorEvent connectorEvent;
	
	/**
	 * Instantiates a new raw data.
	 *
	 * @param data the data that is to be sent or has been received
	 * @deprecated Use one of the other constructors instead.
	 */
	@Deprecated
	public RawData(final byte[] data) {
		this(data, null, 0, null, false);
	}
	
	/**
	 * Instantiates a new raw data.
	 *
	 * @param data the data that is to be sent or has been received
	 * @param address the IP address and port the data is to be sent to or has been received from
	 * @throws NullPointerException if any of the given parameters is <code>null</code>
	 */
	public RawData(final byte[] data, final InetSocketAddress address) {
		this(data, address, null, false);
	}
	
	/**
	 * 
	 * @param event the event that occured
	 * @param address the Address of the event
	 */
	public RawData(final ConnectorEvent event, final InetSocketAddress address) {
		this(new byte[0], address, null, false, event);
	}
	
	/**
	 * Instantiates a new raw data.
	 *
	 * @param data the data that is to be sent or has been received
	 * @param address the IP address and port the data is to be sent to or has been received from
	 * @param clientIdentity the identity of the authenticated sender of the message
	 *     (or <code>null</code> if sender is not authenticated)
	 * @throws NullPointerException if any of the given parameters is <code>null</code>
	 */
	public RawData(final byte[] data, final InetSocketAddress address, final Principal clientIdentity) {
		this(data, address, clientIdentity, false);
	}
	
	/**
	 * Instantiates a new raw data.
	 *
	 * @param data the data that is to be sent or has been received
	 * @param address the IP address the data is to be sent to or has been received from
	 * @param port the port the data is to be sent to or has been received from
	 * @throws NullPointerException if data is <code>null</code>
	 */
	public RawData(final byte[] data, final InetAddress address, final int port) {
		this(data, address, port, null, false);
	}
	
	/**
	 * Instantiates a new raw data.
	 *
	 * @param data the data that is to be sent or has been received
	 * @param address the IP address the data is to be sent to or has been received from
	 * @param port the port the data is to be sent to or has been received from
	 * @param clientIdentity the identity of the authenticated sender of the message
	 *     (or <code>null</code> if sender is not authenticated)
	 * @throws NullPointerException if data is <code>null</code>
	 */
	public RawData(final byte[] data, final InetAddress address, final int port, final Principal clientIdentity) {
		this(data, address, port, clientIdentity, false);
	}
	
	/**
	 * Instantiates a new raw data.
	 *
	 * @param data the data that is to be sent or has been received
	 * @param address the IP address and port the data is to be sent to or has been received from
	 * @param multicast indicates whether the data represents a multicast message
	 * @throws NullPointerException if data or address is <code>null</code>
	 */
	public RawData(final byte[] data, final InetSocketAddress address, final boolean multicast) {
		this(data, address, null, multicast);
	}

	/**
	 * Instantiates a new raw data.
	 *
	 * @param data the data that is to be sent or has been received
	 * @param address the IP address and port the data is to be sent to or has been received from
	 * @param clientIdentity the identity of the authenticated sender of the message
	 *     (or <code>null</code> if sender is not authenticated)
	 * @param multicast indicates whether the data represents a multicast message
	 * @throws NullPointerException if data or address is <code>null</code>
	 */
	public RawData(final byte[] data, final InetSocketAddress address, final Principal clientIdentity, final boolean multicast) {
		this(data, address, clientIdentity, multicast, null);
	}
	
	public RawData(final byte[] data, final InetSocketAddress address, final Principal clientIdentity, final boolean multicast, final ConnectorEvent event) {
		if (data == null) {
			throw new NullPointerException("Data must not be null");
		}
		if (address == null) {
			throw new NullPointerException("Address must not be null");
		}
		this.bytes = data;
		this.address = address;
		this.senderIdentity = clientIdentity;
		this.multicast = multicast;
		this.messageType = event == null ? MessageType.COMMUNICATION : MessageType.EVENT;
		this.connectorEvent = event;
		
	}
	
	/**
	 * Instantiates a new raw data.
	 *
	 * @param data the data that is to be sent or has been received
	 * @param address the IP address the data is to be sent to or has been received from
	 * @param port the port the data is to be sent to or has been received from
	 * @param multicast indicates whether the data represents a multicast message
	 * @throws NullPointerException if data is <code>null</code>
	 */
	public RawData(final byte[] data, final InetAddress address, final int port, final boolean multicast) {
		this(data, address, port, null, multicast);
	}
	
	/**
	 * Instantiates a new raw data.
	 *
	 * @param data the data that is to be sent or has been received
	 * @param address the IP address the data is to be sent to or has been received from
	 * @param port the port the data is to be sent to or has been received from
	 * @param clientIdentity the identity of the authenticated sender of the message
	 *     (or <code>null</code> if sender is not authenticated)
	 * @param multicast indicates whether the data represents a multicast message
	 * @throws NullPointerException if data is <code>null</code>
	 */
	public RawData(final byte[] data, final InetAddress address, final int port, final Principal clientIdentity, final boolean multicast) {
		this(data, new InetSocketAddress(address, port), clientIdentity, multicast);
	}
	
	/**
	 * Gets the raw message.
	 *
	 * @return a copy of the raw message bytes
	 */
	public byte[] getBytes() {
		return Arrays.copyOf(bytes, bytes.length);
	}
	
	/**
	 * Gets the length of the serialized message
	 *
	 * @return the size
	 */
	public int getSize() {
		return bytes.length;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public InetAddress getAddress() {
		return address.getAddress();
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 * @deprecated Use constructor instead.
	 */
	@Deprecated
	public void setAddress(final InetAddress newAddress) {
		this.address = new InetSocketAddress(newAddress, address.getPort());
	}

	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public int getPort() {
		return address.getPort();
	}

	/**
	 * Sets the port.
	 *
	 * @param port the new port
	 * @deprecated Use constructor instead.
	 */
	@Deprecated
	public void setPort(final int port) {
		this.address = new InetSocketAddress(address.getAddress(), port);
	}

	/**
	 * Checks if this is a multicast message
	 *
	 * @return true, if this is a multicast message
	 */
	public boolean isMulticast() {
		return multicast;
	}

	/**
	 * Marks this message as a multicast message.
	 *
	 * @param multicast whether this message is a multicast message
	 * @deprecated Use constructor instead.
	 */
	@Deprecated
	public void setMulticast(final boolean multicast) {
		this.multicast = multicast;
	}
	
	/**
	 * Gets the source/destination IP address and port.
	 *
	 * @return the address
	 */
	public InetSocketAddress getInetSocketAddress() {
		return address;
	}
	
	/**
	 * Gets the identity of the sender of the message.
	 * 
	 * This property is only meaningful for messages
	 * received from a client.
	 * 
	 * @return the identity or <code>null</code> if the
	 *      sender has not been authenticated
	 */
	public Principal getSenderIdentity() {
		return senderIdentity;
	}
	
	/**
	 * Get the Message type.
	 * 
	 * If Something meaningful happens at the connector level, 
	 * the Type will be Internal, if its a message from a node or
	 * a Client, then it will be External
	 * @return
	 */
	public MessageType getMessageType() {
		return messageType;
	}
	
	
	/**
	 * Get the Type messageless Event
	 * 
	 * If the Message Type is Internal, then there will be 
	 * an event attach to it.
	 * @return
	 */
	public ConnectorEvent getConnectorEvent() {
		return connectorEvent;
	}
}
