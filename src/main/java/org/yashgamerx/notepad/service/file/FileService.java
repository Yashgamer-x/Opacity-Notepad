package org.yashgamerx.notepad.service.file;

/**
 * Composite interface for components that need both read and write access.
 * Extends both ISP-split interfaces so existing consumers are unaffected.
 */
public interface FileService extends FileReader, FileWriter {
}
