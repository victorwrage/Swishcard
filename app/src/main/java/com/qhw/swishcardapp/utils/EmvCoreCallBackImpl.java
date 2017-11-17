package com.qhw.swishcardapp.utils;

import com.pos.api.Icc;
import com.pos.api.Pci;
import com.pos.emvcore.callback.EmvCoreCallBack;
import com.qhw.swishcardapp.device.d2000v.ByteUtil;


public class EmvCoreCallBackImpl implements EmvCoreCallBack {
	
	private Icc mIcc = null;
	private Pci  mPci = null;
	
	private byte mSlot = 0;


	public EmvCoreCallBackImpl(Icc icc, Pci pci) {
		super();
		this.mIcc = icc;
		this.mPci = pci;
	}

	@Override
	public int IccClose(byte slot) {
		// TODO Auto-generated method stub
		int iRet = -1;
		
		if(null == mIcc)
			return iRet;
		
		iRet = mIcc.DLL_IccClose(slot);
		
		System.out.println("IccClose iRet: " + iRet);
		
		return iRet;
	}

	@Override
	public int IccCommand(byte slot, byte [] apduSend, byte [] apduRecv) {
		// TODO Auto-generated method stub
		int iRet = -1;
		
		if(null == mIcc)
			return iRet;
		
		iRet = mIcc.DLL_IccCommand(slot, apduSend, apduRecv);
		
		System.out.println("IccCommand iRet: " + iRet);
		
		return iRet;
	}

	@Override
	public int IccReset(byte slot, byte vccMode, byte [] ATR) {
		// TODO Auto-generated method stub
		int iRet = -1;
		
		System.out.println("Enter IccReset");
		
		if(null == mIcc)
			return iRet;
		
		mSlot = slot;
		
		iRet = mIcc.DLL_IccOpen(slot, vccMode, ATR);
		
		System.out.println("IccReset iRet: " + iRet);
		
		return iRet;
	}

	@Override
	public int OnlinePin() {
		// TODO Auto-generated method stub
		
		System.out.println("OnlinePin");
//		int iRet = -1;
//
//		if(null == mPci)
//			return iRet;
//		
//		
//		byte [] background = new byte[3];
//		byte [] font = new byte [4];
//		byte [] prompt = "Please Cover The Keypad With Hand And Enter PIN:".getBytes();
//		com.pos.utils.PinContext ctx = new com.pos.utils.PinContext();
//		
//		byte [] cardNo = new byte[50];
//		byte [] pinBlock = new byte[8];
//		
//		cardNo = "1234567890123456".getBytes();
//		
//		background[0] = (byte)0xff;//R
//		background[1] = (byte)0xff;//G
//		background[2] = (byte)0xff;//B
//		font[0] = (byte)0x00;//R
//		font[1] = (byte)0x00;//G
//		font[2] = (byte)0x00;//B
//		font[3] = 20;//text size(dp)
//		ctx.setPromptLength(prompt.length);
//		ctx.setFontLength(font.length);
//		ctx.setBackgroundLength(background.length);					
//		ctx.setPrompt(prompt);   
//		ctx.setFont(font);
//		ctx.setBackground(background);
//		
//		iRet = mPci.DLL_PciGetPin((byte) 1, (byte)4, (byte)12, (byte)16, cardNo, (byte)0, pinBlock, (byte)1, "123456.00".getBytes(), (byte)0, ctx);
//		
//		System.out.println("DLL_PciGetPin iRet: " + iRet);
//		
//		return iRet;
		
		return 0;
	}

	@Override
	public int PlainPinVerify(int availTimes) {
		// TODO Auto-generated method stub
		int iRet = -1;
		
		System.out.println("PlainPinVerify availTimes: " + availTimes);

		if(null == mPci)
			return iRet;
		
		
		byte [] background = new byte[3];
		byte [] font = new byte [4];
		byte [] prompt = "Please Cover The Keypad With Hand And Enter PIN:".getBytes();
		com.pos.utils.PinContext ctx = new com.pos.utils.PinContext();

		background[0] = (byte)0xff;//R
		background[1] = (byte)0xff;//G
		background[2] = (byte)0xff;//B
		font[0] = (byte)0x00;//R
		font[1] = (byte)0x00;//G
		font[2] = (byte)0x00;//B
		font[3] = 20;//text size(dp)
		ctx.setPromptLength(prompt.length);
		ctx.setFontLength(font.length);
		ctx.setBackgroundLength(background.length);					
		ctx.setPrompt(prompt);   
		ctx.setFont(font);
		ctx.setBackground(background);
		
		byte minLen = 4;
		byte maxLen = 12;
		
		
		iRet = mPci.DLL_PciOffLinePlainPin(mSlot, minLen, maxLen, (byte) 30, ctx);
		
		System.out.println("DLL_PciOffLinePlainPin iRet: " + iRet);
		
		return iRet;
	}
	
	@Override
	public int EncipherPinVerify(byte[] pk, int availTimes) {
		// TODO Auto-generated method stub
		int iRet = -1;
		
		System.out.println("EncipherPinVerify availTimes: " + availTimes);
		
		if(null == mPci)
			return iRet;
		
		byte [] background = new byte[3];
		byte [] font = new byte [4];
		byte [] prompt = "Please Cover The Keypad With Hand And Enter PIN:".getBytes();
		com.pos.utils.PinContext ctx = new com.pos.utils.PinContext();

		background[0] = (byte)0xff;//R
		background[1] = (byte)0xff;//G
		background[2] = (byte)0xff;//B
		font[0] = (byte)0x00;//R
		font[1] = (byte)0x00;//G
		font[2] = (byte)0x00;//B
		font[3] = 20;//text size(dp)
		ctx.setPromptLength(prompt.length);
		ctx.setFontLength(font.length);
		ctx.setBackgroundLength(background.length);					
		ctx.setPrompt(prompt);   
		ctx.setFont(font);
		ctx.setBackground(background);
		
		System.out.println("EncipherPinVerify pk: " + ByteUtil.bytearrayToHexString(pk, 512));
		
		byte minLen = 4;
		byte maxLen = 12;
		
		System.out.println("invoke DLL_PciOffLinePlainPin ");
		
		iRet = mPci.DLL_PciOffLineEncPin(mSlot, minLen, maxLen, (byte) 30, pk, ctx);
		System.out.println("DLL_PciOffLinePlainPin iRet: " + iRet);
		
		return iRet;
	}

}
