package org.davincischools.leo.server.utils.work_queue;

import com.google.protobuf.Message;

public interface Worker<T extends Message> {

  void submitWork(T payload);

  void process(T payload) throws Exception;

  void failed(T payload) throws Exception;
}
