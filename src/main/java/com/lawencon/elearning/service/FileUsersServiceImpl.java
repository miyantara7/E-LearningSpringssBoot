package com.lawencon.elearning.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.lawencon.elearning.dao.FileUsersDao;
import com.lawencon.elearning.model.FileQuestionHeader;
import com.lawencon.elearning.model.FileUsers;
import com.lawencon.elearning.model.Users;

@Service
@Transactional
public class FileUsersServiceImpl implements FileUsersService {

	@Autowired
	private FileUsersDao fileUjianService;

	@Override
	public boolean insertUjian(MultipartFile file, String soalHeader, String user) throws Exception {
		long millis = System.currentTimeMillis();
		FileUsers fileUjian = null;
		FileUsers fileNew = new FileUsers();
		FileQuestionHeader header = new FileQuestionHeader();
		Users u = new Users();
		fileNew.setFile(file.getBytes());
		fileNew.setFileName(file.getOriginalFilename());
		fileNew.setFileType(file.getContentType());
		LocalTime time = LocalTime.now();
		java.sql.Date date = new java.sql.Date(millis);
		if (time.compareTo(fileUjianService.findTimeExam(soalHeader)) > 0) {
			return false;
		} else {
			try {
				try {
					fileUjian = fileUjianService.findBySoalHeader(user, soalHeader);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (fileUjian != null) {
					fileUjian.setFile(file.getBytes());
					fileUjian.setFileType(file.getContentType());
					fileUjian.setFileName(file.getOriginalFilename());
					fileUjianService.update(fileUjian);
					return true;
				} else {
					fileNew.setDate(date);
					header.setHeaderQuestionId(soalHeader);
					fileNew.setFileQuestionHeader(header);
					u.setId(user);
					fileNew.setUser(u);
					fileUjianService.insert(fileNew);
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception();
			}

		}
	}

	@Override
	public FileUsers update(FileUsers file) throws Exception {
		return fileUjianService.update(file);
	}

	@Override
	public FileUsers findById(String id) throws Exception {
		return fileUjianService.findById(id);
	}

	@Override
	public boolean insertTask(String idUser, String idHeader, MultipartFile file) throws Exception {
		long millis = System.currentTimeMillis();
		FileUsers cekFile = null;
		FileUsers fileUsers = new FileUsers();
		FileQuestionHeader header = new FileQuestionHeader();
		Users user = new Users();
		LocalDate dates = LocalDate.now();
		java.sql.Date date = new java.sql.Date(millis);
		Date dts = java.sql.Date.valueOf( dates );
		if (dts.after(fileUjianService.findDateTask(idHeader))) {
			return false;
		} else if (dts.before(fileUjianService.findDateTask(idHeader))) {
			header.setHeaderQuestionId(idHeader);
			fileUsers.setFile(file.getBytes());
			fileUsers.setFileName(file.getOriginalFilename());
			fileUsers.setFileType(file.getContentType());
			fileUsers.setDate(date);
			user.setId(idUser);
			fileUsers.setUser(user);
			try {
				cekFile = fileUjianService.findBySoalHeader(idUser, idHeader);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (cekFile == null) {
				fileUsers.setFileQuestionHeader(header);
				fileUjianService.insert(fileUsers);
			} else {
				cekFile.setFile(file.getBytes());
				cekFile.setFileName(file.getOriginalFilename());
				cekFile.setFileType(file.getContentType());
				cekFile.setDate(date);
				fileUjianService.update(cekFile);
			}
			return true;
		} else if (dts.equals(fileUjianService.findDateTask(idHeader))) {
			header.setHeaderQuestionId(idHeader);
			fileUsers.setFile(file.getBytes());
			fileUsers.setFileName(file.getOriginalFilename());
			fileUsers.setFileType(file.getContentType());
			fileUsers.setDate(date);
			user.setId(idUser);
			fileUsers.setUser(user);
			try {
				cekFile = fileUjianService.findBySoalHeader(idUser, idHeader);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (cekFile == null) {
				fileUsers.setFileQuestionHeader(header);
				fileUjianService.insert(fileUsers);
			} else {
				cekFile.setFile(file.getBytes());
				cekFile.setFileName(file.getOriginalFilename());
				cekFile.setFileType(file.getContentType());
				cekFile.setDate(date);
				fileUjianService.update(cekFile);
			}
			return true;
		}else {
			throw new Exception();
		}

	}

	@Override
	public void deleteFileUserByHeaderId(String id) throws Exception {
		fileUjianService.deleteFileByHeaderId(id);

	}

	@Override
	public List<Map<String, Object>> viewFileUser(String kelasId, String userId) throws Exception {
		return fileUjianService.viewFileuser(kelasId, userId);
	}

}
