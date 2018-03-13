package com.ibamb.udm.net;

/**
 * Created by luotao on 18-3-9.
 */

public interface ICommandCoder {
    public byte[] encodeSetParameterCommand(String type,String value);
    public byte[] encodeGetParameterCommand(String type);
}
