package com.app.blog.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.blog.dto.PostDTO;
import com.app.blog.dto.UpdatePostDTO;
import com.app.blog.models.Posts;
import com.app.blog.models.Users;
import com.app.blog.repository.PostRepository;
import com.app.blog.util.EntitiyHawk;
import com.app.blog.util.JWTUtils;
import com.app.blog.util.PostMapper;

import io.jsonwebtoken.Claims;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 1460344
 */
@RestController
@RequestMapping("/api")
public class GlobalController extends EntitiyHawk {
	
	@Autowired
	private JWTUtils jwtUtils;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private PostMapper postMapper;
	
	@GetMapping("/getPostCount")
	public ResponseEntity getPostCount(HttpServletRequest request) throws Exception {

		Users user = null;

		String username = null;

		String jwtToken = null;

		// get jwt token
		String requestTokenHeader = request.getHeader("authorization");

		if(requestTokenHeader==null || !requestTokenHeader.startsWith("Bearer ")) {

			throw new Exception("Invalid Token");
		}

		jwtToken = requestTokenHeader.substring(7);

		try {

			// username = jwtUtil.extractUsername(jwtToken);

			user = jwtUtils.getUser(jwtToken);

		} catch (Exception e) {
			e.printStackTrace();
			
			throw new Exception(e);
		}

		return genericResponse(user.getPostsList().size());
	}
	
	
	@PostMapping("/publish")
	public ResponseEntity<Object> postCart(HttpServletRequest request, @RequestBody PostDTO postDto) throws Exception {

		Users user = null;

		String username = null;

		String jwtToken = null;
		
		Posts posts = null;

		// get jwt token
		String requestTokenHeader = request.getHeader("authorization");

		if(requestTokenHeader==null || !requestTokenHeader.startsWith("Bearer ")) {

			throw new Exception("Invalid Token");
		}

		jwtToken = requestTokenHeader.substring(7);

		try {

			// username = jwtUtil.extractUsername(jwtToken);

			user = jwtUtils.getUser(jwtToken);
			
			posts = new Posts();
			
			if(postDto.getBody()==null || postDto.getBody().equals("")) {				
				return genericError("body should not be empty");
			}
			
			
			posts.setPostTitle(postDto.getTitle());
			posts.setPostBody(postDto.getBody());
			posts.setPublishedBy(user);
			
			postRepository.save(posts);
			
		} catch (Exception e) {
			e.printStackTrace();
			
			throw new Exception(e);
		}

		return genericResponse("Published");
	}
	
	@GetMapping("/getPost")
	public ResponseEntity getPost(HttpServletRequest request) throws Exception {

		Users user = null;

		String username = null;

		String jwtToken = null;
		
		List<Posts> posts = null;
		
		// List<PostDTO> postDtos = null;

		List<Map> postMaps = null;
		
		// get jwt token
		String requestTokenHeader = request.getHeader("authorization");

		if(requestTokenHeader==null || !requestTokenHeader.startsWith("Bearer ")) {

			throw new Exception("Invalid Token");
		}

		jwtToken = requestTokenHeader.substring(7);

		try {

			// username = jwtUtil.extractUsername(jwtToken);

			user = jwtUtils.getUser(jwtToken);
			
			posts = postRepository.getPostByPublishedBy(user);
			
			// postDtos = new ArrayList<PostDTO>();
			
			/*
			for(Posts post:  posts) {
				
				PostDTO tempPostDto = new PostDTO();
				tempPostDto.setTitle(post.getPostTitle());
				tempPostDto.setBody(post.getPostBody());
				postDtos.add(tempPostDto);
			}
			*/
			
			/*
			postDtos = posts.stream()
			        .map(post -> {
			            PostDTO tempPostDto = new PostDTO();
			            tempPostDto.setTitle(post.getPostTitle());
			            tempPostDto.setBody(post.getPostBody());
			            return tempPostDto;
			        })
			        .collect(Collectors.toCollection(ArrayList::new));
			*/
			/*
			for(Posts post:  posts) {
				
				Map tempPostMap = new HashMap<>();
				tempPostMap = postMapper.postDetailsToMap(post);
				postMaps.add(tempPostMap);
			}
			*/
			postMaps = posts.stream()
			        .map(post -> postMapper.postDetailsToMap(post))
			        .collect(Collectors.toList());
			
					
		} catch (Exception e) {
			e.printStackTrace();
			
			throw new Exception(e);
		}

		return genericResponse(postMaps);
	}
	
	@GetMapping("/getPost/{postID}")
	public ResponseEntity<Object> getPost(HttpServletRequest request, @PathVariable int postID) throws Exception {

		Posts post = null;
		
		Map postMap = null;
		
		try {
			
			post = postRepository.getPostByPostId(postID);
			
			if(post!=null) {
				postMap = postMapper.postDetailsToMap(post);
			}else {
				return genericError("Post Not Found");
			}
			
					
		} catch (Exception e) {
			e.printStackTrace();
			
			throw new Exception(e);
		}

		return genericResponse(postMap);
	}
	
	@GetMapping("/getPostByUser/{userId}")
	public ResponseEntity<Object> getPostByUser(HttpServletRequest request, @PathVariable int userId) throws Exception {

		Users user = null;

		String jwtToken = null;
		
		List<Posts> posts = null;
		
		// List<PostDTO> postDtos = null;

		List<Map> postMaps = null;
		
		// get jwt token
		String requestTokenHeader = request.getHeader("authorization");

		if(requestTokenHeader==null || !requestTokenHeader.startsWith("Bearer ")) {

			throw new Exception("Invalid Token");
		}

		jwtToken = requestTokenHeader.substring(7);

		try {

			// username = jwtUtil.extractUsername(jwtToken);

			user = jwtUtils.getUser(jwtToken);
			
			if(user.getUserId() == userId) {
				posts = postRepository.getPostByPublishedBy(user);
				
				postMaps = posts.stream()
				        .map(post -> postMapper.postDetailsToMap(post))
				        .collect(Collectors.toList());
			}
			
			if(posts == null || posts.isEmpty()) {
				
				return genericError("No posts by user Id "+userId);
			}
			
					
		} catch (Exception e) {
			e.printStackTrace();
			
			throw new Exception(e);
		}

		return genericResponse(postMaps);
	}
	
	
	
	
	@PostMapping("/updatePost")
	public ResponseEntity<Object> updatePost(HttpServletRequest request, @RequestBody UpdatePostDTO updatePostDTO) throws Exception {

		Users user = null;

		String jwtToken = null;
		
		Posts postToUpdate = null;

		// get jwt token
		String requestTokenHeader = request.getHeader("authorization");

		if(requestTokenHeader==null || !requestTokenHeader.startsWith("Bearer ")) {

			throw new Exception("Invalid Token");
		}

		jwtToken = requestTokenHeader.substring(7);

		try {

			// username = jwtUtil.extractUsername(jwtToken);

			user = jwtUtils.getUser(jwtToken);
			
			postToUpdate = postRepository.getPostByPostId(updatePostDTO.getPost_id());
			
			if(postToUpdate.getPublishedBy().getUserId() == user.getUserId()) {				
				postToUpdate.setPostBody(updatePostDTO.getBody());
				postRepository.save(postToUpdate);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
			throw new Exception(e);
		}

		return genericResponse("Post updated");
	}
	
	
	@GetMapping("/deletePost/{postID}")
	public ResponseEntity<Object> deletePost(HttpServletRequest request, @PathVariable int postID) throws Exception {

		Users user = null;

		String jwtToken = null;
		
		Posts postToDelete = null;
				
		// get jwt token
		String requestTokenHeader = request.getHeader("authorization");

		if(requestTokenHeader==null || !requestTokenHeader.startsWith("Bearer ")) {

			throw new Exception("Invalid Token");
		}

		jwtToken = requestTokenHeader.substring(7);

		try {

			// username = jwtUtil.extractUsername(jwtToken);

			user = jwtUtils.getUser(jwtToken);
			
			postToDelete = postRepository.getPostByPostId(postID);
			
			if(user.getUserId() == postToDelete.getPublishedBy().getUserId()) {
				postRepository.delete(postToDelete);
			}
					
		} catch (Exception e) {
			e.printStackTrace();
			
			throw new Exception(e);
		}

		return genericResponse("Post Deleted");
	}

}
