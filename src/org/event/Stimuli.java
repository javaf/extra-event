// @wolfram77
package org.event;

// required modules
import org.data.*;
import java.util.*;
import java.lang.reflect.*;
import java.util.concurrent.*;



/**
 * Represents a set of stimulus with associated reactions
 * @author wolfram77
 */
public class Stimuli extends ConcurrentHashMap<String, Set<Reactable>> implements Runnable {
    
    // data
    String stimulus;
    Map args;
    
    // static data
    static Reactable fallback = new DefReaction();
    static ExecutorService threads = Executors.newCachedThreadPool();
    
    
    /**
     * Convert a string from camel case to hyphen case
     * @param str camel case string
     * @return hyphen case string
     */
    String _toHyphenCase(String str) {
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
     * Add static / instance reaction methods of a class
     * @param obj object containing methods (null if static)
     * @param cls class containing methods
     */
    private void _onClass(Object obj, Class cls) {
        boolean bestatic = obj==null;
        for(Method m : cls.getMethods()) {
            // need static or instance?
            String mthd = m.getName();
            boolean isstatic = Modifier.isStatic(m.getModifiers());
            if(!mthd.startsWith("on") || (!isstatic && bestatic)) continue;
            // save appropriately
            String stim = _toHyphenCase(mthd.substring(2));
            Reaction reaction = bestatic? new Reaction(cls, mthd) : new Reaction(obj, mthd);
            if(m.isAnnotationPresent(Reacts.class)) reaction.speed(m.getAnnotation(Reacts.class).speed());
            on(stim, reaction);
        }
    }
    
    
    /**
     * Initialize stimulus set before adding
     * @param stimulus name of stimulus
     */
    void _initStimulusSet(String stimulus) {
        if(get(stimulus) != null) return;
        put(stimulus, Collections.newSetFromMap(new ConcurrentHashMap<Reactable, Boolean>()));
    }
    
    
    /**
     * Indicate a stimulus (emit event)
     * @param stimulus name of stimulus
     * @param args additional arguments
     */
    void _is(String stimulus, Map args) {
        Set<Reactable> reactions = get(stimulus);
        if(reactions == null) fallback.on(stimulus, args);
        else reactions.stream().forEach((e) -> {
            e.on(stimulus, args);
        });
    }
    
    
    /**
     * Set fallback reaction, that is called when a stimulus no other reaction
     * @param fallback fallback reaction
     */
    public static void fallback(Reactable fallback) {
        Stimuli.fallback = fallback;
    }
    
    
    /**
     * Get fallback reaction
     * @return fallback reaction
     */
    public static Reactable fallback() {
        return fallback;
    }
    
    
    /**
     * Create {@linkplain Stimuli}
     */
    public Stimuli() {
    }
    
    
    /**
     * Create {@linkplain Stimuli} from class
     * @param cls class containing reaction methods
     */
    public Stimuli(Class cls) {
        _onClass(null, cls);
    }
    
    
    /**
     * Create {@linkplain Stimuli} from object
     * @param obj object containing reaction methods
     */
    public Stimuli(Object obj) {
        _onClass(obj, obj.getClass());
    }
    
    
    /**
     * Tell stimuli speed ("fast" or "slow")
     * @param speed stimuli speed
     * @return {@linkplain Stimuli}
     */
    public Stimuli speed(String speed) {
        stimulus = speed.equals("fast")? "" : null;
        return this;
    }
    
    
    /**
     * Get stimuli speed ("fast" or "slow")
     * @return stimuli speed
     */
    public String speed() {
        return stimulus != null? "fast" : "slow";
    }

    
    
    
    /**
     * DONT CALL THIS!
     * Indicate a stimulus asynchronously (emit event)
     */
    @Override
    public void run() {
        _is(stimulus, args);
    }
    
    
    /**
     * Indicate a stimulus (emit event)
     * @param stimulus name of stimulus
     * @param args additional arguments
     * @return stimuli
     */
    public Stimuli is(String stimulus, Map args) {
        if(this.stimulus == null) { _is(stimulus, args); return this; }
        this.stimulus = stimulus;
        this.args = args;
        return this;
    }
    
    
    /**
     * Indicate a stimulus (emit event)
     * @param stimulus name of stimulus
     * @param args additional arguments
     * @return stimuli
     */
    public Stimuli is(String stimulus, Object... args) {
        return is(stimulus, Coll.map(args));
    }
    
    
    /**
     * Add a reaction on a stimulus
     * @param stimulus name of stimulus
     * @param reaction reaction
     * @return stimuli
     */
    public Stimuli on(String stimulus, Reactable reaction) {
        _initStimulusSet(stimulus);
        get(stimulus).add(reaction);
        return this;
    }
    
    
    /**
     * Add some reactions on a stimulus
     * @param stimulus name of stimulus
     * @param reactions collection of reactions
     * @return stimuli
     */
    public Stimuli on(String stimulus, Collection<Reactable> reactions) {
        _initStimulusSet(stimulus);
        get(stimulus).addAll(reactions);
        return this;
    }
    
    
    /**
     * Add a reaction on some stimuli
     * @param stimuli collection of stimulus
     * @param reaction reaction
     * @return stimuli
     */
    public Stimuli on(Collection<String> stimuli, Reactable reaction) {
        stimuli.stream().forEach((stim) -> {
            on(stim, reaction);
        });
        return this;
    }
    
    
    /**
     * Add some reactions on some stimuli
     * @param map stimuli with associated reactions
     * @return stimuli
     */
    public Stimuli on(Map<String, Set<Reactable>> map) {
        map.keySet().stream().forEach((stim) -> {
            on(stim, map.get(stim));
        });
        return this;
    }
    
    
    /**
     * Remove a reaction from a stimulus
     * @param stimulus name of stimulus
     * @param reaction reaction
     * @return stimuli
     */
    public Stimuli off(String stimulus, Reactable reaction) {
        Set<Reactable> s = get(stimulus);
        if(s != null) s.remove(reaction);
        return this;
    }
    
    
    /**
     * Remove some reactions from a stimulus
     * @param stimulus name of stimulus
     * @param reactions collections of reactions
     * @return stimuli
     */
    public Stimuli off(String stimulus, Collection<Reactable> reactions) {
        Set<Reactable> e = get(stimulus);
        if(e != null) e.removeAll(reactions);
        return this;
    }
    
    
    // Off (events, eventer)
    // - remove eventer from events
    /**
     * Remove a reaction from some stimuli
     * @param stimuli collection of stimulus
     * @param reaction reaction
     * @return stimuli
     */
    public Stimuli off(Collection<String> stimuli, Reactable reaction) {
        stimuli.stream().forEach((stim) -> {
            off(stim, reaction);
        });
        return this;
    }
    
    
    /**
     * Remove some reactions from some stimuli
     * @param map stimuli associated with reactions
     * @return stimuli
     */
    public Stimuli off(Map<String, Set<Reactable>> map) {
        map.keySet().stream().forEach((stim) -> {
            off(stim, map.get(stim));
        });
        return this;
    }
    
    
    /**
     * Remove all reactions from a stimulus
     * @param stimulus name of stimulus
     * @return stimuli
     */
    public Stimuli off(String stimulus) {
        remove(stimulus);
        return this;
    }
    
    
    /**
     * Remove all reactions from all stimuli
     * @return stimuli
     */
    public Stimuli off() {
        clear();
        return this;
    }
}
