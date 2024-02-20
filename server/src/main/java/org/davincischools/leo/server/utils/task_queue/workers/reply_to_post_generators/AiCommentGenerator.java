package org.davincischools.leo.server.utils.task_queue.workers.reply_to_post_generators;

import java.io.IOException;

public interface AiCommentGenerator {
  void generateComment(CommentGeneratorIo generatorIo) throws IOException;
}
