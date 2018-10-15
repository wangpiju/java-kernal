package com.hs3.utils.auth;

import com.hs3.utils.ListUtils;
import com.hs3.web.utils.WebUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AuthUtils {
    public static final String AUTH_KEY = "AUTH_KEY";

    public static void saveAuth(HttpServletRequest request, List<String> urls) {
        /**jd-gui
         Set<String> list = new HashSet();
         Iterator localIterator2;
         for (Iterator localIterator1 = urls.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
         {
         String u = (String)localIterator1.next();
         List<String> us = ListUtils.toList(u);
         localIterator2 = us.iterator(); continue;String nu = (String)localIterator2.next();
         list.add(nu);
         }
         HttpSession session = request.getSession();
         session.setAttribute("AUTH_KEY", list);*/

        Set<String> list = new HashSet();
        for (Iterator iterator = urls.iterator(); iterator.hasNext(); ) {
            String u = (String) iterator.next();
            List<String> us = ListUtils.toList(u);
            String nu;
            for (Iterator iterator1 = us.iterator(); iterator1.hasNext(); list.add(nu))
                nu = (String) iterator1.next();

        }

        HttpSession session = request.getSession();
        session.setAttribute("AUTH_KEY", list);

    }

    public static boolean hasAuth(HttpServletRequest request) {
        String url = WebUtils.getUrl(request);
        return hasAuth(request, url);
    }

    public static boolean hasAuth(HttpServletRequest request, String url) {
        HttpSession session = request.getSession();
        Object obj = session.getAttribute("AUTH_KEY");
        if (obj != null) {
            Set<String> list = (Set) obj;
            for (String p : list) {
                if (url.startsWith(p)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasAuthOne(HttpServletRequest request, List<String> url) {
        //jd-gui
    /*HttpSession session = request.getSession();
    Object obj = session.getAttribute("AUTH_KEY");
    if (obj != null)
    {
      Set<String> list = (Set)obj;
      Iterator localIterator2;
      for (Iterator localIterator1 = url.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
      {
        String u = (String)localIterator1.next();
        localIterator2 = list.iterator(); continue;String p = (String)localIterator2.next();
        if (u.startsWith(p)) {
          return true;
        }
      }
    }
    return false;*/

        HttpSession session = request.getSession();
        Object obj = session.getAttribute("AUTH_KEY");
        if (obj != null) {
            Set<String> list = (Set) obj;
            for (Iterator iterator = url.iterator(); iterator.hasNext(); ) {
                String u = (String) iterator.next();
                for (Iterator iterator1 = list.iterator(); iterator1.hasNext(); ) {
                    String p = (String) iterator1.next();
                    if (u.startsWith(p))
                        return true;
                }

            }

        }
        return false;
    }

    public static boolean hasAuthAll(HttpServletRequest request, List<String> url) {
        HttpSession session = request.getSession();
        Object obj = session.getAttribute("AUTH_KEY");
        boolean has = false;
        if (obj != null) {
            Set<String> list = (Set) obj;
            for (String u : url) {
                has = false;
                for (String p : list) {
                    if (u.startsWith(p)) {
                        has = true;
                        break;
                    }
                }
                if (!has) {
                    return false;
                }
            }
        }
        return has;
    }
}
