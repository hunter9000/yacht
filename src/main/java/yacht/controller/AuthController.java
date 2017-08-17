package yacht.controller;

import yacht.interceptor.RolePermissions;
import yacht.model.user.Role;
import yacht.model.user.RoleType;
import yacht.model.user.User;
import yacht.repository.RoleRepository;
import yacht.repository.UserRepository;
import yacht.request.AuthRequest;
import yacht.response.StringResponse;
import yacht.security.JwtSubject;
import yacht.security.SecurityManager;
import yacht.util.AuthUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SecurityManager securityManager;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static final String BAD_LOGIN_MESSAGE = "Incorrect username or password";

    @RequestMapping(value="/api/authenticate/", method=RequestMethod.POST, produces="application/json")
    @ResponseBody
    public ResponseEntity<StringResponse> authenticate(@RequestBody AuthRequest request) {
        System.out.println(request.name+ ": " + request.password);

        // find the user with the given username
        User user = userRepository.findByUsername(request.name);
        if (user == null) {
            return new ResponseEntity<>(new StringResponse(BAD_LOGIN_MESSAGE), HttpStatus.UNAUTHORIZED);        // user not found
        }

        // validate the password
        boolean pwMatch = passwordEncoder.matches(request.password, user.getPassword());
        if (!pwMatch) {
            return new ResponseEntity<>(new StringResponse(BAD_LOGIN_MESSAGE), HttpStatus.UNAUTHORIZED);
        }

        // create the jwt token
        JwtSubject subject = new JwtSubject();
        subject.setUserId(user.getId());
        List<RoleType> roles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            roles.add(role.getRoleName());
        }
        subject.setRoles(roles);
        subject.setUserName(user.getUsername());

        String s = null;
        try {
            s = subject.getAsJSON();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new StringResponse("Error processing user"), HttpStatus.BAD_REQUEST);
        }

        // generate jwt
        String jwtToken = Jwts.builder().setSubject(s).signWith(SignatureAlgorithm.HS512, securityManager.getSecurityKey()).compact();

        System.out.println(jwtToken);

        // pass the jwt back in the response
        return new ResponseEntity<>(new StringResponse(jwtToken), HttpStatus.OK);
    }

    @RequestMapping(value="/api/roles/", method = RequestMethod.GET)
    @RolePermissions(allowedRoles = {RoleType.ADMIN})
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @RequestMapping(value="/api/user/", method = RequestMethod.POST)
    @RolePermissions(allowedRoles = {RoleType.ADMIN})
    public ResponseEntity<StringResponse> createUser(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty() ||
                user.getEmail() == null || user.getEmail().isEmpty() || user.getRoles() == null || user.getRoles().isEmpty()) {
            return new ResponseEntity<>(new StringResponse("Invalid data"), HttpStatus.BAD_REQUEST);
        }
        // hash the given password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);      // save to db
        return new ResponseEntity<>(new StringResponse("success"), HttpStatus.OK);
    }

    // gets all users in the system
    @RequestMapping(value="/api/users/", method=RequestMethod.GET)
    @RolePermissions(allowedRoles = {RoleType.ADMIN})
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    // gets the specific user
    @RequestMapping(value="/api/users/{userId}/", method=RequestMethod.GET)
    @RolePermissions(allowedRoles = {RoleType.ADMIN})
    public User getUser(@PathVariable Long userId) {
        return userRepository.findOne(userId);
    }

    // deletes the specified user
    @RequestMapping(value="/api/users/{userId}/", method=RequestMethod.DELETE)
    @RolePermissions(allowedRoles = {RoleType.ADMIN})
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userRepository.delete(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // updates the given user
    @RequestMapping(value="/api/users/{userId}/", method=RequestMethod.PUT)
    @RolePermissions(allowedRoles = {RoleType.ADMIN})
    public User editUser(@PathVariable Long userId, @RequestBody User userRequest) {
        User user = userRepository.findOne(userId);
        if (userRequest.getPassword() != null && !userRequest.getPassword().equals("")) {            // if a new pw was supplied
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()) );
        }
        user.setEmail(userRequest.getEmail());
        user.setRoles(userRequest.getRoles());
        user = userRepository.save(user);
        return user;
    }

    // gets the currently logged in user's User object
    @RequestMapping(value="/api/profile/", method=RequestMethod.GET)
    public User getProfile() {
        return userRepository.findOne(AuthUtils.getUserId(request));
    }

    // updates the currently logged in user's User object
    @RequestMapping(value="/api/profile/", method=RequestMethod.PUT)
    public User editProfile(@RequestBody User userRequest) {
        User user = userRepository.findOne(AuthUtils.getUserId(request));
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()) );
        user.setEmail(userRequest.getEmail());
        user = userRepository.save(user);
        return user;
    }



//    @RequestMapping(value="/api/authenticate", method= RequestMethod.POST)
//    public ResponseEntity<?> greeting( @RequestBody AuthRequest request) {
//        System.out.println(request.name+ ": " + request.password);
//
//        User u = userRepository.findByUsername(request.name);
//
//        if (u != null) {
//
//            JwtSubject subject = new JwtSubject();
//            subject.setUserId(u.getId());
//
//            String s = null;
//            try {
//                s = subject.getAsJSON();
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//                return new ResponseEntity<>(new SuccessResponse(false, "Error processing user"), HttpStatus.BAD_REQUEST);
//            }
//
//            // generate jwt
//            String jwtToken = Jwts.builder().setSubject(s).signWith(SignatureAlgorithm.HS512, securityManager.getSecurityKey()).compact();
//
//            System.out.println(jwtToken);
//
//            return new ResponseEntity<>(new AuthResponse(true, jwtToken), HttpStatus.OK);
////            return new AuthResponse(true, jwtToken);
//        }
//
//        // user not found
//        return new ResponseEntity<>(new AuthResponse(false, null), HttpStatus.UNAUTHORIZED);
////        return new AuthResponse(false, null);
//    }
//
//    @RequestMapping(value="/api/charselect/{charId}", method = RequestMethod.POST)
//    public ResponseEntity selectCharacter(@PathVariable long charId) {
//
//        JwtSubject token = (JwtSubject)request.getAttribute("jwtToken");
//
//        // TODO verify charId belongs to this user
//        token.setCharId(charId);
//
//        String s = null;
//        try {
//            s = token.getAsJSON();
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(new SuccessResponse(false, "Error processing user"), HttpStatus.BAD_REQUEST);
//        }
//
//        // generate jwt
//        String jwtToken = Jwts.builder().setSubject(s).signWith(SignatureAlgorithm.HS512, securityManager.getSecurityKey()).compact();
//
//        System.out.println(jwtToken);
//
//        return new ResponseEntity<>(new AuthResponse(true, jwtToken), HttpStatus.OK);
//    }


}