/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/pgautam/workspace-posit-odk/AdhocJava/src/org/hfoss/adhoc/IAdhocService.aidl
 */
package org.hfoss.adhoc;
import java.lang.String;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;
import android.os.Parcel;
public interface IAdhocService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.hfoss.adhoc.IAdhocService
{
private static final java.lang.String DESCRIPTOR = "org.hfoss.adhoc.IAdhocService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an IAdhocService interface,
 * generating a proxy if needed.
 */
public static org.hfoss.adhoc.IAdhocService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.hfoss.adhoc.IAdhocService))) {
return ((org.hfoss.adhoc.IAdhocService)iin);
}
return new org.hfoss.adhoc.IAdhocService.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getPort:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getPort();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.hfoss.adhoc.IAdhocService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public int getPort() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPort, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getPort = (IBinder.FIRST_CALL_TRANSACTION + 0);
}
public int getPort() throws android.os.RemoteException;
}
