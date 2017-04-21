package com.example;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by TSH on 2017/3/23.
 */

public class FileUtils {
	public static void print(String s) {
		BufferedOutputStream outputStream = null;

		try {
			outputStream = new BufferedOutputStream(
					new FileOutputStream(new File("E:/log.txt"), true));

			outputStream.write(s.getBytes());
			outputStream.write("\n".getBytes());
			outputStream.flush();


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
