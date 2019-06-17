package com.bj.scb.weixin;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class PictureMessage extends BaseMessage {

	@XStreamAlias("Image")
	private ImageMessage image;
	
	public ImageMessage getImageMessage() {
		return image;
	}

	public void setImageMessage(ImageMessage image) {
		this.image = image;
	}

	public PictureMessage(Map<String, String> requestMap, ImageMessage image) {
		super(requestMap);
		this.setMsgType("image");
		this.image = image;
	}

	/**
	 * @category 内部图片类
	 * @author JDRY-SJM
	 *
	 */
	@XStreamAlias("xml")
	public static class ImageMessage {
		@XStreamAlias("MediaId")
		private String mediaId;
		
		public ImageMessage(String mediaId) {
			super();
			this.mediaId = mediaId;
		}

		public String getMediaId() {
			return mediaId;
		}

		public void setMediaId(String mediaId) {
			this.mediaId = mediaId;
		}

	}

}
