// @wolfram77
package org.event;

// required modules
import java.util.*;
import java.lang.reflect.*;
import java.util.concurrent.*;



/**
 * Represents stimuli with associated reflexes. <p>
 * When a stimulus occurs, appropriate reflexes are triggered. </p>
 * @author wolfram77
 */
public class Spine extends ConcurrentHashMap<String, Set<Reflexive>> {
    
    // static data
    static Reflexive fallback = (String stimulus, Map args) -> {
        System.out.println("["+stimulus+"] : "+args);
    };
    
    
    /**
     * Convert a string from camel case to hyphen case.
     * @param str camel case string
     * @return hyphen case string
     */
    String _hyphenCase(String str) {
        StringBuilder s = new StringBuilder();
        for(int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if(c >= 'A' && c<='Z') {
                if(i > 0) s.append('-');
                s.append((char)(c-'A'+'a'));
            }
            else s.append(c);
        }
        return s.toString();
    }
    
    
    /**
     * Add static / instance reflex methods of a class.
     * @param obj object containing reflex methods (null if static)
     * @param cls class containing reflex methods
     */
    private void _onClass(Object obj, Class cls) {
        boolean bestatic = obj==null;
        for(Method m : cls.getMethods()) {
            // need static or instance?
            String mthd = m.getName();
            boolean isstatic = Modifier.isStatic(m.getModifiers());
            if(mthd.length()<=2 || !mthd.startsWith("on") || isstatic != bestatic) continue;
            // save appropriately
            String stim = _hyphenCase(mthd.substring(2));
            Reflex reflex = isstatic? new Reflex(cls, mthd) : new Reflex(obj, mthd);
            on(stim, reflex);
        }
    }
    
    
    /**
     * Initialize reflex set of stimulus.
     * @param stimulus name of stimulus
     */
    void _initStimulus(String stimulus) {
        if(get(stimulus) != null) return;
        put(stimulus, Collections.newSetFromMap(new ConcurrentHashMap<Reflexive, Boolean>()));
    }
    
    
    /**
     * Set fallback reflex, for stimuli with no reflex.
     * @param fallback fallback reflex
     */
    public static void fallback(Reflexive fallback) {
        Spine.fallback = fallback;
    }
    
    
    /**
     * Get fallback reflex, for stimuli with no reflex.
     * @return fallback reflex
     */
    public static Reflexive fallback() {
        return fallback;
    }
    
    
    /**
     * Create a spine. <p>
     * Associate reflexes to stimuli with {@code on(...)}. </p>
     */
    public Spine() {
    }
    
    
    /**
     * Create a spine from class. <p>
     * {@code on<stimulus>()} static methods are associated {@code <stimulus>} stimulus. <br>
     * Use {@code @Speed("slow")} annotation to indicate slow reflex methods. </p>
     * @param cls class containing reflex methods
     */
    public Spine(Class cls) {
        _onClass(null, cls);
    }
    
    
    /**
     * Create a spine from object. <p>
     * {@code on<stimulus>()} instance methods are associated {@code <stimulus>} stimulus. <br>
     * Use {@code @Speed("slow")} annotation to indicate slow reflex methods. </p>
     * @param obj object containing reflex methods
     */
    public Spine(Object obj) {
        _onClass(obj, obj.getClass());
    }
    
    
    /**
     * Indicate a stimulus, causing reflexes to trigger.
     * @param stimulus name of stimulus
     * @param args additional arguments
     * @return spine for chaining
     */
    public Spine is(String stimulus, Map args) {
        Set<Reflexive> s = get(stimulus);
        if(s == null || s.isEmpty()) fallback.on(stimulus, args);
        else s.stream().forEach((r) -> {
            r.on(stimulus, args);
        });
        return this;
    }
    
    
    /**
     * Indicate a stimulus, causing reflexes to trigger.
     * @param stimulus name of stimulus
     * @param args additional arguments
     * @return spine for chaining
     */
    public Spine is(String stimulus, Object... args) {
        Map<Object, Object> margs = new HashMap<>();
        for(int i=1; i<args.length; i+=2)
            margs.put(args[i-1], args[i]);
        return is(stimulus, margs);
    }
    
    
    /**
     * Set a reflex to trigger on a stimulus.
     * @param stimulus name of stimulus
     * @param reflex reflex to trigger
     * @return spine for chaining
     */
    public Spine on(String stimulus, Reflexive reflex) {
        _initStimulus(stimulus);
        get(stimulus).add(reflex);
        return this;
    }
    
    
    /**
     * Set reflexes to trigger on a stimulus.
     * @param stimulus name of stimulus
     * @param reflexes reflexes to trigger
     * @return spine for chaining
     */
    public Spine on(String stimulus, Collection<Reflexive> reflexes) {
        _initStimulus(stimulus);
        get(stimulus).addAll(reflexes);
        return this;
    }
    
    
    /**
     * Set a reflex to trigger on stimuli.
     * @param stimuli names of stimuli
     * @param reflex reflex to trigger
     * @return spine for chaining
     */
    public Spine on(Collection<String> stimuli, Reflexive reflex) {
        stimuli.stream().forEach((stim) -> {
            on(stim, reflex);
        });
        return this;
    }
    
    
    /**
     * Set associated reflexes to trigger on stimuli.
     * @param assoc stimuli with associated reflexes to trigger
     * @return spine for chaining
     */
    public Spine on(Map<String, ? extends Collection<Reflexive>> assoc) {
        assoc.entrySet().stream().forEach((e) -> {
            on(e.getKey(), e.getValue());
        });
        return this;
    }
    
    
    /**
     * Turn off a reflex for a stimulus.
     * @param stimulus name of stimulus
     * @param reflex reflex to turn off
     * @return spine for chaining
     */
    public Spine off(String stimulus, Reflexive reflex) {
        Set<Reflexive> s = get(stimulus);
        if(s != null) s.remove(reflex);
        return this;
    }
    
    
    /**
     * Turn off reflexes for a stimulus.
     * @param stimulus name of stimulus
     * @param reflexes reflexes to turn off
     * @return spine for chaining
     */
    public Spine off(String stimulus, Collection<Reflexive> reflexes) {
        Set<Reflexive> s = get(stimulus);
        if(s != null) s.removeAll(reflexes);
        return this;
    }
    
    
    /**
     * Turn off a reflex for stimuli.
     * @param stimuli names of stimuli
     * @param reflex reflex to turn off
     * @return spine for chaining
     */
    public Spine off(Collection<String> stimuli, Reflexive reflex) {
        stimuli.stream().forEach((stim) -> {
            off(stim, reflex);
        });
        return this;
    }
    
    
    /**
     * Turn off associated reflexes for stimuli.
     * @param assoc stimuli with associated reflexes to turn off
     * @return spine for chaining
     */
    public Spine off(Map<String, ? extends Collection<Reflexive>> assoc) {
        assoc.entrySet().stream().forEach((e) -> {
            off(e.getKey(), e.getValue());
        });
        return this;
    }
    
    
    /**
     * Turn off all reflexes for a stimulus.
     * @param stimulus name of stimulus
     * @return spine for chaining
     */
    public Spine off(String stimulus) {
        remove(stimulus);
        return this;
    }
    
    
    /**
     * Turn off all reflexes for all stimuli.
     * @return spine for chaining
     */
    public Spine off() {
        clear();
        return this;
    }
}
