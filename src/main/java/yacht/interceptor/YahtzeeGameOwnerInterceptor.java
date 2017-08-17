package yacht.interceptor;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import yacht.model.YahtzeeGame;
import yacht.model.user.User;
import yacht.repository.UserRepository;
import yacht.repository.YahtzeeGameRepository;
import yacht.security.BadRequestException;
import yacht.security.ForbiddenAccessException;
import yacht.util.AuthUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class YahtzeeGameOwnerInterceptor implements HandlerInterceptor {

    @Autowired
    private YahtzeeGameRepository yahtzeeGameRepository;

    @Autowired
    private UserRepository userRepository;

    private Logger logger = Logger.getLogger(YahtzeeGameOwnerInterceptor.class);

    /** Validates that the game being requested is owned by the logged in user. Only applies if the @YahtzeeGameOwnerRequired annotation is on the method */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // check the annotation on the handler method. if @YahtzeeGameOwnerRequired isn't present, return true
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        YahtzeeGameOwnerRequired characterSheetOwnerRequired = handlerMethod.getMethod().getAnnotation(YahtzeeGameOwnerRequired.class);
        if (characterSheetOwnerRequired == null) {
            return true;
        }

        logger.debug("prehandling YahtzeeGameOwnerInterceptor");

        Map<String, String> pathVariables = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        Long gameId = Long.valueOf(pathVariables.get("gameId"));
        if (gameId == null) {
            throw new BadRequestException("No gameId parameter provided");
        }
        YahtzeeGame yahtzeeGame = yahtzeeGameRepository.findOne(gameId);
        if (yahtzeeGame == null) {
            throw new BadRequestException("gameId parameter invalid");
        }


        User user = AuthUtils.getLoggedInUser(request);
        if (!yahtzeeGame.getUser().getId().equals(user.getId())) {
            logger.error("user doesn't match: " + yahtzeeGame.getUser() + " " + user);
            throw new ForbiddenAccessException();
        }

        // store charsheet in request so controllers can access it without looking up again
        request.setAttribute(AuthUtils.GAME, yahtzeeGame);
        return true;
    }

//    private String parseCharId(String uri) {
//        if (uri.startsWith("/api/char/")) {
//            String[] parts = uri.split("/");
//            return parts[3];
//        }
//        else {
//            return null;
//        }
//    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
